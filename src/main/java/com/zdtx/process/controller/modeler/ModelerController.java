package com.zdtx.process.controller.modeler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zdtx.process.domain.modeler.Modeler;
import com.zdtx.process.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.model.Artifact;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 流程模型控制器
 */
@Api(value = "流程模型控制器API")
@Controller
public class ModelerController {

    protected static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Resource
    ProcessEngine processEngine;


    /***
     * 跳转到流程展示界面（带流程数据）
     * @param modelAndView
     * @return
     */
    @ApiOperation(value = "跳转到流程展示界面（内部测试用）")
    @RequestMapping(value = "/index", method = RequestMethod.GET)

    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        modelAndView.addObject("modelList", repositoryService.createModelQuery().list());
        return modelAndView;
    }

    /***
     * 获取所有的流程模型数据
     * @return
     */
    @ApiOperation(value = "获取所有的流程模型数据")
    @RequestMapping(value = "/modelList", method = RequestMethod.GET)
    public RestResponse<List<Modeler>> modelList() {
        try {
            List<Modeler> modelers = new ArrayList<Modeler>();
            List<Model> models = repositoryService.createModelQuery().list();
            if (models.size() > 0) {
                for (Model model : models) {
                    Modeler modeler = new Modeler();
                    modeler.setId(model.getId());
                    modeler.setKey(model.getKey());
                    modeler.setName(model.getName());
                    modeler.setVersion(model.getVersion().toString());
                    modelers.add(modeler);
                }
            }
            return new RestResponse().succuess(modelers);
        } catch (Exception ex) {
            ex.printStackTrace();
            return RestResponse.fail(ex.getMessage());
        }
    }

    /**
     * 跳转编辑器页面
     *
     * @return
     */
    @ApiOperation(value = "跳转流程编辑器页面")
    @GetMapping("/editor")
    public String editor(@ApiParam(value = "模型ID", name = "modelId") String modelId) {
        return "modeler";
    }


    /**
     * 创建模型
     *
     * @param response
     * @param name     模型名称
     * @param key      模型key
     */
    @ApiOperation(value = "创建流程模型")
    @RequestMapping(value = "/modeler/create", method = RequestMethod.POST)
    public RestResponse create(HttpServletResponse response,
                               @ApiParam(value = "模型名称", name = "name") String name,
                               @ApiParam(value = "模型KEY", name = "key") String key,
                               @ApiParam(value = "描述", name = "description") String description,
                               @ApiParam(value = "版本信息", name = "reversion") String reversion) throws IOException {
        logger.info("创建模型入参name：{},key:{}", name, key);
        Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());

//        response.sendRedirect("/editor?modelId=" + model.getId());

        logger.info("创建模型结束，返回模型ID：{}", model.getId());
        return RestResponse.succuess();
    }

    /**
     * 创建模型时完善ModelEditorSource
     *
     * @param modelId
     */
    @SuppressWarnings("deprecation")
    private void createObjectNode(String modelId) {
        logger.info("创建模型完善ModelEditorSource入参模型ID：{}", modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            logger.info("创建模型时完善ModelEditorSource服务异常：{}", e);
        }
        logger.info("创建模型完善ModelEditorSource结束");
    }

    /**
     * 发布流程
     *
     * @param modelId 模型ID
     * @return
     */
    @ApiOperation(value = "发布流程")
    @ResponseBody
    @RequestMapping(value = "/modeler/publish", method = RequestMethod.GET)
    public RestResponse publish(@ApiParam(value = "模型ID", name = "modelId", required = true) String modelId) {
        logger.info("流程部署入参modelId：{}", modelId);
        try {
            Model modelData = repositoryService.getModel(modelId);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            if (bytes == null) {
                logger.info("部署ID:{}的模型数据为空，请先设计流程并成功保存，再进行发布", modelId);
                return RestResponse.fail("部署ID:{}的模型数据为空，请先设计流程并成功保存，再进行发布");
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addBpmnModel((modelData.getKey() == null ? "temp" : modelData.getKey()) + ".bpmn20.xml", model)
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            return RestResponse.succuess();
        } catch (Exception e) {
            logger.info("部署modelId:{}模型服务异常：{}", modelId, e);
            return RestResponse.fail(e.getMessage());
        }
    }

    /**
     * 撤销流程定义
     *
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ApiOperation(value = "撤销流程定义")
    @ResponseBody
    @RequestMapping(value = "/modeler/revokePublish", method = RequestMethod.GET)
    public RestResponse revokePublish(@ApiParam(value = "模型ID", name = "modelId", required = true) String modelId) {
        logger.info("撤销发布流程入参modelId：{}", modelId);
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                /**
                 * 参数不加true:为普通删除，如果当前规则下有正在执行的流程，则抛异常
                 * 参数加true:为级联删除,会删除和当前规则相关的所有信息，包括历史
                 */
                repositoryService.deleteDeployment(modelData.getDeploymentId(), true);
                return RestResponse.succuess();
            } catch (Exception e) {
                logger.error("撤销已部署流程服务异常：{}", e);
                return RestResponse.fail(e.getMessage());
            }
        } else {
            return RestResponse.fail("提供的模型ID查找不到模型数据");
        }
    }

    /**
     * 删除流程实例
     *
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ApiOperation(value = "删除流程实例")
    @ResponseBody
    @RequestMapping(value = "/modeler/delete", method = RequestMethod.GET)
    public RestResponse deleteProcessInstance(@ApiParam(value = "模型ID", name = "modelId", required = true) String modelId) {
        logger.info("删除流程实例入参modelId：{}", modelId);
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(modelData.getKey()).singleResult();
                if (null != pi) {
                    runtimeService.deleteProcessInstance(pi.getId(), "");
                    historyService.deleteHistoricProcessInstance(pi.getId());
                }
                return RestResponse.succuess();
            } catch (Exception e) {
                logger.error("删除流程实例服务异常：{}", e);
                return RestResponse.fail(e.getMessage());
            }
        } else {
            return RestResponse.fail("提供的模型ID查找不到模型数据");
        }
    }

    /**
     * 挂起流程实例
     *
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ApiOperation(value = "挂起流程实例")
    @ResponseBody
    @RequestMapping(value = "/modeler/suspend", method = RequestMethod.GET)
    public RestResponse suspendProcessInstance(@ApiParam(value = "模型ID", name = "modelId", required = true) String modelId) {
        logger.info("挂起流程实例入参modelId：{}", modelId);
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(modelData.getKey()).singleResult();
                if (null != pi) {
                    runtimeService.suspendProcessInstanceById(pi.getId());
                }
                return RestResponse.succuess();
            } catch (Exception e) {
                logger.error("挂起流程实例服务异常：{}", e);
                return RestResponse.fail(e.getMessage());
            }
        } else {
            return RestResponse.fail("提供的模型ID查找不到模型数据");
        }
    }

    /**
     * 激活流程实例
     *
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ApiOperation(value = "激活流程实例")
    @ResponseBody
    @RequestMapping(value = "/modeler/activate", method = RequestMethod.GET)
    public Object activateProcessInstance(@ApiParam(value = "模型ID", name = "modelId", required = true) String modelId) {

        logger.info("激活流程实例入参modelId：{}", modelId);
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(modelData.getKey()).singleResult();
                if (null != pi) {
                    runtimeService.activateProcessInstanceById(pi.getId());
                }
                return RestResponse.succuess();
            } catch (Exception e) {
                logger.error("激活流程实例服务异常：{}", e);
                return RestResponse.fail(e.getMessage());
            }
        } else {
            return RestResponse.fail("提供的模型ID查找不到模型数据");
        }
    }

    /**
     * 根据指定的流程图，按要求高亮流程图中的节点
     * 如果有指定某个节点高亮，就高亮展示这个节点
     * 如果没有指定某个节点高亮，就高亮展示该流程图已执行节点
     * --存在问题：1、图片不是很清晰；2、底色还不知道如何更改
     *
     * @param processInstanceId 模型ID
     * @param nodeName          节点名称
     * @param result
     * @return
     */
    @ApiOperation(value = "以图片形式打开流程图，并且高亮指定的节点")
    @RequestMapping(value = "/modeler/openProcess", method = RequestMethod.GET)
    public RestResponse openmodeler(@ApiParam(value = "流程实例ID", name = "processInstanceId", required = true) String processInstanceId,
                                    @ApiParam(value = "节点名称", name = "nodeName", required = false) String nodeName,
                                    HttpServletResponse response) {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        try {

            InputStream is = getDiagram(processInstanceId, nodeName);
            if (is == null) {
                return RestResponse.fail("没有获取到流程图片流");
            }

            BufferedImage image = ImageIO.read(is);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);

            is.close();
            out.close();
            return RestResponse.succuess();
        } catch (Exception e) {
            logger.error("获取流程图异常：{}", e);
            return RestResponse.fail(e.getMessage());
        }
    }

    /***
     * 根据流程实例ID获取流程图图片输入流
     * @param processInstanceId
     * @param nodeId 指定节点高亮，为空则当前执行节点高亮
     * @return
     */
    private InputStream getDiagram(String processInstanceId, String nodeName) {
        //获得流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null) {
                return null;
            } else {
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);

        List<String> selectFlowElements = null;
        if (StringUtils.isEmpty(nodeName)) {
            //获取流程实例当前的节点，需要高亮显示
            if (processInstance != null) {
                selectFlowElements = runtimeService.getActiveActivityIds(processInstance.getId());
            }
        } else {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            if (flowElements.size() > 0) {
                for (FlowElement flowElement : flowElements) {
                    if (nodeName.equals(flowElement.getName())) {
                        selectFlowElements = new ArrayList<String>();
                        selectFlowElements.add(flowElement.getId());
                        break;
                    }
                }
            }
        }

        return processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", selectFlowElements, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);
    }
}
