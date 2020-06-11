### 一、工程介绍
### 二、功能介绍
### 三、工程结构
```tcl
├─java
│  └─com
│      └─zdtx
│          └─process
│              ├─config
│              ├─constants
│              ├─controller
│              │  ├─demo
│              │  ├─editor
│              │  ├─leave
│              │  ├─modeler
│              │  ├─system
│              │  └─task
│              ├─domain
│              │  ├─leave
│              │  ├─modeler
│              │  ├─system
│              │  └─task
│              ├─listener
│              ├─mapper
│              │  ├─leave
│              │  └─system
│              ├─receiver
│              ├─service
│              │  ├─base
│              │  │  └─impl
│              │  ├─dept
│              │  │  └─impl
│              │  ├─leave
│              │  ├─modeler
│              │  │  └─impl
│              │  ├─role
│              │  ├─task
│              │  │  └─impl
│              │  └─user
│              │      └─impl
│              └─utils
└─resources
    ├─mapper
    ├─processes
    ├─static
    │  ├─bak
    │  │  ├─bootstrap3
    │  │  │  ├─css
    │  │  │  ├─fonts
    │  │  │  └─js
    │  │  ├─bpmn
    │  │  │  ├─css
    │  │  │  ├─descriptors
    │  │  │  └─vendor
    │  │  │      └─bpmn-font
    │  │  │          ├─css
    │  │  │          └─font
    │  │  └─modeler
    │  │      ├─diagram-viewer
    │  │      │  ├─images
    │  │      │  │  └─deployer
    │  │      │  │      └─blue
    │  │      │  └─js
    │  │      │      └─jquery
    │  │      └─editor-app
    │  │          ├─configuration
    │  │          │  └─properties
    │  │          ├─css
    │  │          ├─editor
    │  │          │  ├─css
    │  │          │  └─i18n
    │  │          ├─fonts
    │  │          ├─i18n
    │  │          ├─images
    │  │          ├─libs
    │  │          │  ├─angular-cookies_1.2.13
    │  │          │  ├─angular-dragdrop_1.0.7
    │  │          │  ├─angular-mocks_1.2.13
    │  │          │  ├─angular-resource_1.2.13
    │  │          │  ├─angular-route_1.2.13
    │  │          │  ├─angular-sanitize_1.2.13
    │  │          │  ├─angular-scroll_0.5.7
    │  │          │  ├─angular-strap_2.0.5
    │  │          │  ├─angular-translate-loader-static-files
    │  │          │  ├─angular-translate-storage-cookie
    │  │          │  ├─angular-translate_2.4.2
    │  │          │  ├─angular_1.2.13
    │  │          │  ├─bootstrap-daterangepicker_1.3.7
    │  │          │  ├─bootstrap_3.1.1
    │  │          │  │  ├─css
    │  │          │  │  ├─fonts
    │  │          │  │  └─js
    │  │          │  ├─es5-shim-15.3.4.5
    │  │          │  │  └─tests
    │  │          │  │      ├─helpers
    │  │          │  │      ├─lib
    │  │          │  │      └─spec
    │  │          │  ├─jquery_1.11.0
    │  │          │  ├─json3_3.2.6
    │  │          │  │  └─lib
    │  │          │  └─momentjs_2.5.1
    │  │          ├─partials
    │  │          ├─popups
    │  │          └─stencilsets
    │  │              └─bpmn2.0
    │  │                  └─icons
    │  │                      ├─activity
    │  │                      │  └─list
    │  │                      ├─artifact
    │  │                      ├─catching
    │  │                      ├─connector
    │  │                      ├─dataobject
    │  │                      ├─endevent
    │  │                      ├─gateway
    │  │                      ├─startevent
    │  │                      ├─swimlane
    │  │                      └─throwing
    │  ├─diagram-viewer
    │  │  ├─images
    │  │  │  └─deployer
    │  │  │      └─blue
    │  │  └─js
    │  │      └─jquery
    │  ├─editor-app
    │  │  ├─configuration
    │  │  │  └─properties
    │  │  ├─css
    │  │  ├─editor
    │  │  │  ├─css
    │  │  │  └─i18n
    │  │  ├─fonts
    │  │  ├─i18n
    │  │  ├─images
    │  │  ├─libs
    │  │  │  ├─angular-cookies_1.2.13
    │  │  │  ├─angular-dragdrop_1.0.7
    │  │  │  ├─angular-ivh-treeview
    │  │  │  ├─angular-mocks_1.2.13
    │  │  │  ├─angular-resource_1.2.13
    │  │  │  ├─angular-route_1.2.13
    │  │  │  ├─angular-sanitize_1.2.13
    │  │  │  ├─angular-scroll_0.5.7
    │  │  │  ├─angular-strap_2.0.5
    │  │  │  ├─angular-translate-loader-static-files
    │  │  │  ├─angular-translate-storage-cookie
    │  │  │  ├─angular-translate_2.4.2
    │  │  │  ├─angular_1.2.13
    │  │  │  ├─bootstrap-daterangepicker_1.3.7
    │  │  │  ├─bootstrap_3.1.1
    │  │  │  │  ├─css
    │  │  │  │  ├─fonts
    │  │  │  │  └─js
    │  │  │  ├─es5-shim-15.3.4.5
    │  │  │  │  └─tests
    │  │  │  │      ├─helpers
    │  │  │  │      ├─lib
    │  │  │  │      └─spec
    │  │  │  ├─jquery_1.11.0
    │  │  │  ├─json3_3.2.6
    │  │  │  │  └─lib
    │  │  │  └─momentjs_2.5.1
    │  │  ├─partials
    │  │  ├─popups
    │  │  └─stencilsets
    │  │      └─bpmn2.0
    │  │          └─icons
    │  │              ├─activity
    │  │              │  └─list
    │  │              ├─artifact
    │  │              ├─catching
    │  │              ├─connector
    │  │              ├─dataobject
    │  │              ├─endevent
    │  │              ├─gateway
    │  │              ├─startevent
    │  │              ├─swimlane
    │  │              └─throwing
    │  ├─images
    │  └─img
    └─templates
        ├─process
        │  ├─definition
        │  ├─general
        │  ├─group
        │  ├─leave
        │  ├─leaveCounterSign
        │  ├─modeler
        │  ├─tDispatchingCars
        │  ├─todoitem
        │  └─user
        └─static
            ├─ajax
            │  └─libs
            │      ├─beautifyhtml
            │      ├─blockUI
            │      ├─bootstrap-fileinput
            │      ├─bootstrap-select
            │      ├─bootstrap-table
            │      │  ├─extensions
            │      │  │  ├─columns
            │      │  │  ├─editable
            │      │  │  ├─export
            │      │  │  ├─mobile
            │      │  │  └─toolbar
            │      │  └─locale
            │      ├─bootstrap-treetable
            │      ├─cropbox
            │      ├─datapicker
            │      ├─duallistbox
            │      ├─fullscreen
            │      ├─iCheck
            │      ├─jasny
            │      ├─jquery-layout
            │      ├─jquery-ztree
            │      │  └─3.5
            │      │      ├─css
            │      │      │  ├─default
            │      │      │  │  └─img
            │      │      │  │      └─diy
            │      │      │  ├─metro
            │      │      │  │  └─img
            │      │      │  └─simple
            │      │      │      └─img
            │      │      └─js
            │      ├─jsonview
            │      ├─layer
            │      │  └─theme
            │      │      ├─default
            │      │      └─moon
            │      ├─layui
            │      │  ├─css
            │      │  │  └─modules
            │      │  │      └─laydate
            │      │  │          └─default
            │      │  │              └─font
            │      │  └─lay
            │      │      └─modules
            │      ├─My97DatePicker
            │      │  └─4.8
            │      │      ├─lang
            │      │      └─skin
            │      │          ├─default
            │      │          ├─twoer
            │      │          └─whyGreen
            │      ├─report
            │      │  ├─echarts
            │      │  ├─peity
            │      │  └─sparkline
            │      ├─select2
            │      ├─staps
            │      ├─suggest
            │      ├─summernote
            │      │  └─font
            │      ├─typeahead
            │      └─validate
            ├─css
            │  ├─main
            │  ├─patterns
            │  └─ticket
            ├─file
            ├─fonts
            ├─i18n
            ├─img
            ├─js
            │  ├─face
            │  ├─fileinput
            │  ├─plugins
            │  │  ├─metisMenu
            │  │  └─slimscroll
            │  └─ticket
            └─zdtx
                ├─css
                └─js
```

