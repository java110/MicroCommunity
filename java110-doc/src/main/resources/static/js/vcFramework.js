/**
 * vcFramework
 *
 * @author 吴学文
 *
 * @version 0.3
 *
 * @description 完成组件化编程思想
 *
 * @time 2020-03-04
 *
 * @qq 928255095
 *
 * @mail 928255095@qq.com
 *
 */
/**
 构建vcFramework对象
 **/
(function(window) {
    "use strict";
    let vcFramework = window.vcFramework || {};
    window.vcFramework = vcFramework;
    //为了兼容 0.1版本的vc 框架
    window.vc = vcFramework;
    let _vmOptions = {};
    let _initMethod = [];
    let _initEvent = [];
    let _component = {};
    let _destroyedMethod = [];
    let _timers = []; //定时器
    let _map = []; // 共享数据存储
    let _namespace = [];
    let _vueCache = {};
    let _routes = []; // 页面路由


    _vmOptions = {
        el: '#component',
        data: {},
        watch: {},
        methods: {},
        destroyed: function() {
            window.vcFramework.destroyedMethod.forEach(function(eventMethod) {
                eventMethod();
            });
            //清理所有定时器

            window.vcFramework.timers.forEach(function(timer) {
                clearInterval(timer);
            });

            _timers = [];
        }

    };

    vcFramework = {
        version: "v0.0.3",
        name: "vcFramework",
        author: '吴学文',
        email: '928255095@qq.com',
        qq: '928255095',
        description: 'vcFramework 是自研的一套组件开发套件',
        vueCache: _vueCache,
        vmOptions: _vmOptions,
        namespace: _namespace,
        initMethod: _initMethod,
        initEvent: _initEvent,
        component: _component,
        destroyedMethod: _destroyedMethod,
        debug: false,
        timers: _timers,
        _map: {},
        pageRoutes: _routes, //路由
    };
    //通知window对象
    window.vcFramework = vcFramework;
    window.vc = vcFramework;

})(window);

(function(vcFramework) {

    let componentCache = {};
    /**
     *  树
     * @param {*} _vcCreate  自定义组件
     * @param {*} _html 组件内容
     * @param {*} _nodeLocation  组件位置 1 开始节点 -1 结束节点
     */
    let VcTree = function(_vcCreate, _html, _nodeLocation) {
        let o = new Object();
        o.treeId = vcFramework.uuid();
        o.vcCreate = _vcCreate;
        o.html = _html;
        o.js = "";
        o.css = "";
        o.vcSubTree = [];
        o.nodeLocation = _nodeLocation;
        o.putSubTree = function(_vcSubTree) {
            o.vcSubTree.push(_vcSubTree);
        };
        o.setHtml = function(_html) {
            o.html = _html;
        };
        o.setJs = function(_js) {
            o.js = _js;
        };
        o.setCss = function(_css) {
            o.css = _css;
        };
        o.setLocation = function(_location) {
            o.nodeLocation = _location;
        };
        return o;
    };

    /**
     * 构建 树
     */
    vcFramework.builderVcTree = async function() {
        let _componentUrl = location.hash;

        //判断是否为组件页面
        if (vcFramework.notNull(_componentUrl)) {
            let _vcComponent = document.getElementById('component');
            let vcComponentChilds = _vcComponent.childNodes;
            for (let vcIndex = vcComponentChilds.length - 1; vcIndex >= 0; vcIndex--) {
                _vcComponent.removeChild(vcComponentChilds[vcIndex]);
            }

            if (_componentUrl.lastIndexOf('#') > -1) {
                let endPos = _componentUrl.length;
                if (_componentUrl.indexOf('?') > -1) {
                    endPos = _componentUrl.indexOf('?');
                }
                _componentUrl = _componentUrl.substring(_componentUrl.lastIndexOf('#') + 1, endPos);
            }

            let _tmpVcCreate = document.createElement("vc:create");
            let _divComponentAttr = document.createAttribute('path');
            _divComponentAttr.value = _componentUrl;
            _tmpVcCreate.setAttributeNode(_divComponentAttr);
            _vcComponent.appendChild(_tmpVcCreate);

            let _commonPath = _vcComponent.getAttribute('vc-path');
            if (vc.isNotEmpty(_commonPath)) {
                let _pathVcCreate = document.createElement("vc:create");
                let _pathDivComponentAttr = document.createAttribute('path');
                _pathDivComponentAttr.value = _commonPath;
                _pathVcCreate.setAttributeNode(_pathDivComponentAttr);
                _vcComponent.appendChild(_pathVcCreate);
            }

        } else {
            let _vcComponent = document.getElementById('component');
            let _commonPath = _vcComponent.getAttribute('vc-path');
            if (vc.isNotEmpty(_commonPath)) {
                let _pathVcCreate = document.createElement("vc:create");
                let _pathDivComponentAttr = document.createAttribute('path');
                _pathDivComponentAttr.value = _commonPath;
                _pathVcCreate.setAttributeNode(_pathDivComponentAttr);
                _vcComponent.appendChild(_pathVcCreate);
            }

        }
        let vcElements = document.getElementsByTagName('vc:create');
        let treeList = [];
        let _componentScript = [];
        for (let _vcElementIndex = 0; _vcElementIndex < vcElements.length; _vcElementIndex++) {
            let _vcElement = vcElements[_vcElementIndex];
            let _tree = new VcTree(_vcElement, '', 1);
            let _vcCreateAttr = document.createAttribute('id');
            _vcCreateAttr.value = _tree.treeId;
            _vcElement.setAttributeNode(_vcCreateAttr);
            treeList.push(_tree);
            //创建div
            await findVcLabel(_tree, _vcElement);
            let _res = _tree.html;
        }

        //渲染组件html
        reader(treeList, _componentScript);

        parseVcI18N();
        //执行组件js
        execScript(treeList, _componentScript);
    };

    /**
     * 页面内 组件跳转
     */
    vcFramework.reBuilderVcTree = async function() {
        let _componentUrl = location.hash;

        //判断是否为组件页面
        if (!vcFramework.notNull(_componentUrl)) {
            vcFramework.toast('程序异常，url没有包含组件');
            return;
        }

        if (_componentUrl.lastIndexOf('#') < 0) {
            vcFramework.toast('程序异常，url包含组件错误');
            return;
        }

        let _vcComponent = document.getElementById('component');
        let vcComponentChilds = _vcComponent.childNodes;
        for (let vcIndex = vcComponentChilds.length - 1; vcIndex >= 0; vcIndex--) {
            _vcComponent.removeChild(vcComponentChilds[vcIndex]);
        }
        let endPos = _componentUrl.length;
        if (_componentUrl.indexOf('?') > -1) {
            endPos = _componentUrl.indexOf('?');
        }

        _componentUrl = _componentUrl.substring(_componentUrl.lastIndexOf('#') + 1, endPos);

        let _tmpVcCreate = document.createElement("vc:create");
        let _divComponentAttr = document.createAttribute('path');
        _divComponentAttr.value = _componentUrl;
        _tmpVcCreate.setAttributeNode(_divComponentAttr);
        _vcComponent.appendChild(_tmpVcCreate);

        let _commonPath = _vcComponent.getAttribute('vc-path');
        if (vc.isNotEmpty(_commonPath)) {
            let _pathVcCreate = document.createElement("vc:create");
            let _pathDivComponentAttr = document.createAttribute('path');
            _pathDivComponentAttr.value = _commonPath;
            _pathVcCreate.setAttributeNode(_pathDivComponentAttr);
            _vcComponent.appendChild(_pathVcCreate);
        }

        let treeList = [];
        let _componentScript = [];

        let _vcElement = _tmpVcCreate;

        let vcElements = _vcComponent.getElementsByTagName('vc:create');

        for (let _vcElementIndex = 0; _vcElementIndex < vcElements.length; _vcElementIndex++) {
            let _vcElement = vcElements[_vcElementIndex];
            let _tree = new VcTree(_vcElement, '', 1);
            let _vcCreateAttr = document.createAttribute('id');
            _vcCreateAttr.value = _tree.treeId;
            _vcElement.setAttributeNode(_vcCreateAttr);
            treeList.push(_tree);
            //创建div
            await findVcLabel(_tree, _vcElement);
        }

        //渲染组件html
        reader(treeList, _componentScript);
        parseVcI18N();
        //执行组件js
        execScript(treeList, _componentScript);
    };

    /**
     * 从当前 HTML中找是否存在 <vc:create path="xxxx"></vc:create> 标签
     */
    findVcLabel = async function(_tree) {
        //查看是否存在子 vc:create 
        let _componentName = _tree.vcCreate.getAttribute('path');
        //console.log('_componentName', _componentName, _tree);
        if (!vcFramework.isNotEmpty(_componentName)) {
            throw '组件未包含path 属性' + _tree.vcCreate.outerHTML;
        }
        //开始加载组件
        let _componentElement = await loadComponent(_componentName, _tree);
        //_tree.setHtml(_componentElement);

        //console.log('_componentElement>>', _componentElement)

        if (vcFramework.isNotNull(_componentElement)) {
            let vcChildElements = _componentElement.getElementsByTagName('vc:create');
            if (vcChildElements.length > 0) {
                let _vcDiv = document.createElement('div');
                for (let _vcChildIndex = 0; _vcChildIndex < vcChildElements.length; _vcChildIndex++) {
                    //console.log('vcChildElements', vcChildElements);
                    let _tmpChildElement = vcChildElements[_vcChildIndex];
                    let _subtree = new VcTree(_tmpChildElement, '', 2);
                    let _vcCreateAttr = document.createAttribute('id');
                    _vcCreateAttr.value = _subtree.treeId;
                    _tmpChildElement.setAttributeNode(_vcCreateAttr);
                    _tree.putSubTree(_subtree);
                    await findVcLabel(_subtree);
                }
            }

        }
    };

    /**
     * 渲染组件 html 页面
     */
    reader = function(_treeList, _componentScript) {
        //console.log('_treeList', _treeList);
        let _header = document.getElementsByTagName('head');
        for (let _treeIndex = 0; _treeIndex < _treeList.length; _treeIndex++) {
            let _tree = _treeList[_treeIndex];
            let _vcCreateEl = document.getElementById(_tree.treeId);
            let _componentHeader = _tree.html.getElementsByTagName('head');
            let _componentBody = _tree.html.getElementsByTagName('body');

            if (_vcCreateEl.hasAttribute("location") && 'head' == _vcCreateEl.getAttribute('location')) {
                let _componentHs = _componentHeader[0].childNodes;
                _header[0].appendChild(_componentHeader[0]);

            } else if (_vcCreateEl.hasAttribute("location") && 'body' == _vcCreateEl.getAttribute('location')) {
                _vcCreateEl.parentNode.replaceChild(_componentHeader[0].childNodes[0], _vcCreateEl);
                let _bodyComponentHs = _componentHeader[0].childNodes;
                for (let _bsIndex = 0; _bsIndex < _bodyComponentHs.length; _bsIndex++) {
                    let _bComponentScript = _bodyComponentHs[_bsIndex];
                    if (_bComponentScript.tagName == 'SCRIPT') {
                        let scriptObj = document.createElement("script");
                        scriptObj.src = _bComponentScript.src;
                        scriptObj.type = "text/javascript";
                        document.getElementsByTagName("body")[0].appendChild(scriptObj);
                    } else {
                        _header[0].appendChild(_bodyComponentHs[_bsIndex]);
                    }

                }

            } else {
                //_vcCreateEl.innerHTML = _componentBody[0].innerHTML;
                //_vcCreateEl.parentNode.replaceChild(_componentBody[0], _vcCreateEl);

                for (let _comBodyIndex = 0; _comBodyIndex < _componentBody.length; _comBodyIndex++) {
                    let _childNodes = _componentBody[_comBodyIndex].childNodes;
                    for (let _tmpChildIndex = 0; _tmpChildIndex < _childNodes.length; _tmpChildIndex++) {
                        _vcCreateEl.parentNode.insertBefore(_childNodes[_tmpChildIndex], _vcCreateEl)
                    }

                }

            }
            //将js 脚本放到 组件 脚本中
            if (vcFramework.isNotEmpty(_tree.js)) {
                _componentScript.push(_tree.js);
            }


            let _tmpSubTrees = _tree.vcSubTree;

            if (_tmpSubTrees != null && _tmpSubTrees.length > 0) {
                reader(_tmpSubTrees, _componentScript);
            }

        }
        /**
         * 执行 页面上的js 文件
         */
        let _tmpScripts = document.head.getElementsByTagName("script");
        let _tmpBody = document.getElementsByTagName('body');
        for (let _scriptsIndex = 0; _scriptsIndex < _tmpScripts.length; _scriptsIndex++) {
            let _tmpScript = _tmpScripts[_scriptsIndex];
            //console.log('_head 中 script', _tmpScript.outerHTML)
            let scriptObj = document.createElement("script");
            scriptObj.src = _tmpScript.src;
            //_tmpScript.parentNode.removeChild(_tmpScript);
            scriptObj.type = "text/javascript";
            _tmpBody[0].appendChild(scriptObj);
        }
    };

    vcFramework.i18n = function(_key, _namespace) {
        if (!window.hasOwnProperty('lang')) {
            return _key;
        }

        let _lang = window.lang;

        if (_namespace && _lang.hasOwnProperty(_namespace)) {
            let _namespaceObj = _lang[_namespace];
            if (_namespaceObj.hasOwnProperty(_key)) {
                return _namespaceObj[_key];
            }
        }

        if (!_lang.hasOwnProperty(_key)) {
            return _key;
        }

        return _lang[_key];
    }

    /**
     * 解析 i18n 标签
     */
    parseVcI18N = function() {
        let _tmpI18N = document.getElementsByTagName("vc:i18n");
        for (let _vcElementIndex = 0; _vcElementIndex < _tmpI18N.length; _vcElementIndex++) {
            let _vcElement = _tmpI18N[_vcElementIndex];
            let _name = _vcElement.getAttribute('name');
            let _namespace = _vcElement.getAttribute('namespace');
            let textNode = document.createTextNode(vc.i18n(_name, _namespace));
            _vcElement.parentNode.appendChild(textNode);
            //_vcElement.parentNode.replaceChild(textNode,_vcElement);

        }
        let _i18nLength = _tmpI18N.length;
        for (let _vcElementIndex = 0; _vcElementIndex < _i18nLength; _vcElementIndex++) {
            let _vcElement = _tmpI18N[0];
            _vcElement.parentNode.removeChild(_vcElement);
        }
        _tmpI18N = document.head.getElementsByTagName("vc:i18n");
        for (let _vcElementIndex = 0; _vcElementIndex < _tmpI18N.length; _vcElementIndex++) {
            let _vcElement = _tmpI18N[_vcElementIndex];
            let _name = _vcElement.getAttribute('name');
            let _namespace = _vcElement.getAttribute('namespace');
            let textNode = document.createTextNode(vc.i18n(_name, _namespace));
            _vcElement.parentNode.appendChild(textNode);

        }
        for (let _vcElementIndex = 0; _vcElementIndex < _tmpI18N.length; _vcElementIndex++) {
            let _vcElement = _tmpI18N[_vcElementIndex];
            _vcElement.parentNode.removeChild(_vcElement);
        }

    }

    /**
     * 手工执行js 脚本
     */
    execScript = function(_tree, _componentScript) {

        //console.log('_componentScript', _componentScript);


        for (let i = 0; i < _componentScript.length; i++) {
            //一段一段执行script 
            try {
                eval(_componentScript[i]);
            } catch (e) {
                console.log('js脚本错误', _componentScript[i]);
                console.error(e);
            }

        }

        //初始化vue 对象
        vcFramework.initVue();

        vcFramework.initVcComponent();
    }

    /**
     * 加载组件
     * 异步去服务端 拉去HTML 和 js
     */
    loadComponent = async function(_componentName, _tree) {
        if (vcFramework.isNotEmpty(_componentName) && _componentName.lastIndexOf('/') > 0) {
            _componentName = _componentName + '/' + _componentName.substring(_componentName.lastIndexOf('/') + 1, _componentName.length);
        }
        //从缓存查询
        let _cacheComponent = vcFramework.getComponent(_componentName);
        //console.log('加载组件名称', _componentName);
        let _htmlBody = '';
        let _jsBody = '';
        if (!vcFramework.isNotNull(_cacheComponent)) {
            let _domain = 'components';
            let filePath = '';

            if (_tree.vcCreate.hasAttribute("domain")) {
                _domain = _tree.vcCreate.getAttribute("domain");
            }
            if (_componentName.startsWith('/pages')) { //这里是为了处理 pages 页面
                filePath = _componentName;
            } else { //这里是为了处理组件
                filePath = '/' + _domain + '/' + _componentName;
            }
            let htmlFilePath = filePath + ".html";
            let jsFilePath = filePath + ".js";
            //加载html 页面
            [_htmlBody, _jsBody] = await Promise.all([vcFramework.httpGet(htmlFilePath), vcFramework.httpGet(jsFilePath)]);
            let _componentObj = {
                html: _htmlBody,
                js: _jsBody
            };
            vcFramework.putComponent(_componentName, _componentObj);
        } else {
            _htmlBody = _cacheComponent.html;
            _jsBody = _cacheComponent.js;
        }
        //处理命名空间
        _htmlBody = dealHtmlNamespace(_tree, _htmlBody);

        //处理 js
        _jsBody = dealJs(_tree, _jsBody);
        _jsBody = dealJsAddComponentCode(_tree, _jsBody);
        //处理命名空间
        _jsBody = dealJsNamespace(_tree, _jsBody);

        //处理侦听
        _jsBody = dealHtmlJs(_tree, _jsBody);

        _tmpJsBody = '<script type="text/javascript">//<![CDATA[\n' + _jsBody + '//]]>\n</script>';
        let parser = new DOMParser();

        //let htmlComponentDoc = parser.parseFromString(_htmlBody + _tmpJsBody, 'text/html').documentElement;
        let htmlComponentDoc = parser.parseFromString(_htmlBody, 'text/html').documentElement;

        //创建div
        let vcDiv = document.createElement('div');
        let _divComponentAttr = document.createAttribute('data-component');
        _divComponentAttr.value = _componentName;
        vcDiv.setAttributeNode(_divComponentAttr);
        vcDiv.appendChild(htmlComponentDoc);
        //vcDiv.appendChild(jsComponentDoc);

        _tree.setHtml(vcDiv);
        _tree.setJs(_jsBody);
        return vcDiv;
    };

    /**
     * 处理 命名空间html
     */
    dealHtmlNamespace = function(_tree, _html) {

        let _componentVcCreate = _tree.vcCreate;
        if (!_componentVcCreate.hasAttribute('namespace')) {
            return _html;
        }

        let _namespaceValue = _componentVcCreate.getAttribute("namespace");

        _html = _html.replace(/this./g, _namespaceValue + "_");

        _html = _html.replace(/(id)( )*=( )*'/g, "id='" + _namespaceValue + "_");
        _html = _html.replace(/(id)( )*=( )*"/g, 'id="' + _namespaceValue + '_');
        return _html;
    };
    /**
     * 处理js
     */
    dealJs = function(_tree, _js) {
        //在js 中检测propTypes 属性
        if (_js.indexOf("propTypes") < 0) {
            return _js;
        }

        let _componentVcCreate = _tree.vcCreate;

        //解析propTypes信息
        let tmpProTypes = _js.substring(_js.indexOf("propTypes"), _js.length);
        tmpProTypes = tmpProTypes.substring(tmpProTypes.indexOf("{") + 1, tmpProTypes.indexOf("}")).trim();

        if (!vcFramework.notNull(tmpProTypes)) {
            return _js;
        }

        tmpProTypes = tmpProTypes.indexOf("\r") > 0 ? tmpProTypes.replace("\r/g", "") : tmpProTypes;

        let tmpType = tmpProTypes.indexOf("\n") > 0 ?
            tmpProTypes.split("\n") :
            tmpProTypes.split(",");
        let propsJs = "\nlet $props = {};\n";
        for (let typeIndex = 0; typeIndex < tmpType.length; typeIndex++) {
            let type = tmpType[typeIndex];
            if (!vcFramework.notNull(type) || type.indexOf(":") < 0) {
                continue;
            }
            let types = type.split(":");
            let attrKey = "";
            if (types[0].indexOf("//") > 0) {
                attrKey = types[0].substring(0, types[0].indexOf("//"));
            }
            attrKey = types[0].replace(" ", "");
            attrKey = attrKey.replace("\n", "")
            attrKey = attrKey.replace("\r", "").trim();
            if (!_componentVcCreate.hasAttribute(attrKey) && types[1].indexOf("=") < 0) {
                let componentName = _componentVcCreate.getAttribute("path");
                throw "组件[" + componentName + "]未配置组件属性" + attrKey;
            }
            let vcType = _componentVcCreate.getAttribute(attrKey);
            if (!_componentVcCreate.hasAttribute(attrKey) && types[1].indexOf("=") > 0) {
                vcType = dealJsPropTypesDefault(types[1]);
            } else if (types[1].indexOf("vc.propTypes.string") >= 0) {
                vcType = "'" + vcType + "'";
            }
            propsJs = propsJs + "$props." + attrKey + "=" + vcType + ";\n";
        }

        //将propsJs 插入到 第一个 { 之后
        let position = _js.indexOf("{");
        if (position < 0) {
            let componentName = _componentVcCreate.getAttribute("name");
            throw "组件" + componentName + "对应js 未包含 {}  ";
        }
        let newJs = _js.substring(0, position + 1);
        newJs = newJs + propsJs;
        newJs = newJs + _js.substring(position + 1, _js.length);
        return newJs;
    };

    dealJsPropTypesDefault = function(typeValue) {
        let startPos = typeValue.indexOf("=") + 1;
        let endPos = typeValue.length;
        if (typeValue.indexOf(",") > 0) {
            endPos = typeValue.indexOf(",");
        } else if (typeValue.indexOf("//") > 0) {
            endPos = typeValue.indexOf("//");
        }

        return typeValue.substring(startPos, endPos);
    };
    /**
     * js 处理命名
     */
    dealJsNamespace = function(_tree, _js) {

        //在js 中检测propTypes 属性
        let _componentVcCreate = _tree.vcCreate;

        if (_js.indexOf("vc.extends") < 0) {
            return _js;
        }
        let namespace = "";
        if (!_componentVcCreate.hasAttribute("namespace")) {
            namespace = 'default';
        } else {
            namespace = _componentVcCreate.getAttribute("namespace");
        }

        //js对象中插入namespace 值
        let extPos = _js.indexOf("vc.extends");
        let tmpProTypes = _js.substring(extPos, _js.length);
        let pos = tmpProTypes.indexOf("{") + 1;
        _js = _js.substring(0, extPos) + tmpProTypes.substring(0, pos).trim() +
            "\nnamespace:'" + namespace.trim() + "',\n" + tmpProTypes.substring(pos, tmpProTypes.length);
        let position = _js.indexOf("{");
        let propsJs = "\nlet $namespace='" + namespace.trim() + "';\n";

        let newJs = _js.substring(0, position + 1);
        newJs = newJs + propsJs;
        newJs = newJs + _js.substring(position + 1, _js.length);
        return newJs;
    };

    /**
     * 处理js 变量和 方法都加入 组件编码
     *
     * @param tag 页面元素
     * @param js  js文件内容
     * @return js 文件内容
     */
    dealJsAddComponentCode = function(_tree, _js) {
        let _componentVcCreate = _tree.vcCreate;

        if (!_componentVcCreate.hasAttribute("code")) {
            return _js;
        }

        let code = _componentVcCreate.getAttribute("code");

        return _js.replace("@vc_/g", code);
    }

    /**
     * 处理命名空间js
     */
    dealHtmlJs = function(_tree, _js) {
        let _componentVcCreate = _tree.vcCreate;
        if (!_componentVcCreate.hasAttribute('namespace')) {
            return _js;
        }

        let _namespaceValue = _componentVcCreate.getAttribute("namespace");
        _js = _js.replace(/this./g, "vc.component." + _namespaceValue + "_");
        _js = _js.replace(/(\$)( )*(\()( )*'#/g, "\$('#" + _namespaceValue + "_");

        _js = _js.replace(/(\$)( )*(\()( )*"#/g, "\$(\"#" + _namespaceValue + "_");

        //将 监听也做优化
        _js = _js.replace(/(vc.on)\('/g, "vc.on('" + _namespaceValue + "','");
        _js = _js.replace(/(vc.on)\("/g, "vc.on(\"" + _namespaceValue + "\",\"");
        return _js;
    }

})(window.vcFramework);

/**
 * vc-event 事件处理
 *
 */

(function(vcFramework) {

    _initVcFrameworkEvent = function() {
        let vcFrameworkEvent = document.createEvent('Event');
        // 定义事件名为'build'.
        vcFrameworkEvent.initEvent('initVcFrameworkFinish', true, true);
        vcFramework.vcFrameworkEvent = vcFrameworkEvent;
    };

    /**
     * 初始化 vue 事件
     */
    _initVueEvent = function() {
        vcFramework.$event = new Vue();
    }

    _initVcFrameworkEvent();

    _initVueEvent();


})(window.vcFramework);

/**
 * vc-util
 */
(function(vcFramework) {

    //空判断 true 为非空 false 为空
    vcFramework.isNotNull = function(_paramObj) {
        if (_paramObj == null || _paramObj == undefined) {
            return false;
        }
        return true;
    };

    //空判断 true 为非空 false 为空
    vcFramework.isNotEmpty = function(_paramObj) {
        if (_paramObj == null || _paramObj == undefined || _paramObj.trim() == '') {
            return false;
        }
        return true;
    };

    vcFramework.uuid = function() {
        let s = [];
        let hexDigits = "0123456789abcdef";
        for (let i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        let uuid = s.join("");
        return uuid;
    };

    /**
     * 深度拷贝对象
     */
    vcFramework.deepClone = function(obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    vcFramework.changeNumMoneyToChinese = function(money) {
        let cnNums = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); //汉字的数字
        let cnIntRadice = new Array("", "拾", "佰", "仟"); //基本单位
        let cnIntUnits = new Array("", "万", "亿", "兆"); //对应整数部分扩展单位
        let cnDecUnits = new Array("角", "分", "毫", "厘"); //对应小数部分单位
        let cnInteger = "整"; //整数金额时后面跟的字符
        let cnIntLast = "元"; //整型完以后的单位
        let maxNum = 999999999999999.9999; //最大处理的数字
        let IntegerNum; //金额整数部分
        let DecimalNum; //金额小数部分
        let ChineseStr = ""; //输出的中文金额字符串
        let parts; //分离金额后用的数组，预定义    
        let Symbol = ""; //正负值标记
        if (money == "") {
            return "";
        }

        money = parseFloat(money);
        if (money >= maxNum) {
            alert('超出最大处理数字');
            return "";
        }
        if (money == 0) {
            ChineseStr = cnNums[0] + cnIntLast + cnInteger;
            return ChineseStr;
        }
        if (money < 0) {
            money = -money;
            Symbol = "负 ";
        }
        money = money.toString(); //转换为字符串
        if (money.indexOf(".") == -1) {
            IntegerNum = money;
            DecimalNum = '';
        } else {
            parts = money.split(".");
            IntegerNum = parts[0];
            DecimalNum = parts[1].substr(0, 4);
        }
        if (parseInt(IntegerNum, 10) > 0) { //获取整型部分转换
            var zeroCount = 0;
            var IntLen = IntegerNum.length;
            for (var i = 0; i < IntLen; i++) {
                var n = IntegerNum.substr(i, 1);
                var p = IntLen - i - 1;
                var q = p / 4;
                var m = p % 4;
                if (n == "0") {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        ChineseStr += cnNums[0];
                    }
                    zeroCount = 0; //归零
                    ChineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
                }
                if (m == 0 && zeroCount < 4) {
                    ChineseStr += cnIntUnits[q];
                }
            }
            ChineseStr += cnIntLast;
            //整型部分处理完毕
        }
        if (DecimalNum != '') { //小数部分
            var decLen = DecimalNum.length;
            for (var i = 0; i < decLen; i++) {
                var n = DecimalNum.substr(i, 1);
                if (n != '0') {
                    ChineseStr += cnNums[Number(n)] + cnDecUnits[i];
                }
            }
        }
        if (ChineseStr == '') {
            ChineseStr += cnNums[0] + cnIntLast + cnInteger;
        } else if (DecimalNum == '') {
            ChineseStr += cnInteger;
        }
        ChineseStr = Symbol + ChineseStr;

        return ChineseStr;
    }


})(window.vcFramework);

/**
 * 封装 后端请求 代码
 */
(function(vcFramework) {

    vcFramework.httpGet = function(url) {
        // XMLHttpRequest对象用于在后台与服务器交换数据 
        return new Promise((resolve, reject) => {
            let xhr = new XMLHttpRequest();
            xhr.open('GET', url, true);
            xhr.onreadystatechange = function() {
                // readyState == 4说明请求已完成
                if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
                    // 从服务器获得数据 
                    // fn.call(this, xhr.responseText);
                    resolve(xhr.responseText);
                }
            };
            xhr.send();
        });
    };
    vcFramework.httpPost = function(url, data, fn) {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        // 添加http头，发送信息至服务器时内容编码类型
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                fn.call(xhr.responseText);
            }
        };
        xhr.send(data);
    };
})(window.vcFramework);

/**
 *  vc-cache
 *
 * 组件缓存
 */
(function(vcFramework) {

    /**
     * 组件缓存
     */
    vcFramework.putComponent = function(_componentName, _component) {
        let _componentCache = vcFramework.vueCache;
        _componentCache[_componentName] = _component;
    };
    /**
     * 组件提取
     */
    vcFramework.getComponent = function(_componentName) {
        let _componentCache = vcFramework.vueCache;
        return _componentCache[_componentName];
    }

})(window.vcFramework);

/***
 * vc- constant 内容
 */


/**
 常量
 **/
(function(vcFramework) {

    let constant = {
        REQUIRED_MSG: "不能为空",
        GET_CACHE_URL: ["/user.getUserInfo"]
    }
    vcFramework.constant = constant;
})(window.vcFramework);


/***
 * vc component 0.1 版本代码合并过来-----------------------------------------------------------------------
 *
 *
 */

/**
 vc 函数初始化
 add by Kevin
 **/
(function(vcFramework) {
    let DEFAULT_NAMESPACE = "default";
    vcFramework.http = {
        post: function(componentCode, componentMethod, param, options, successCallback, errorCallback) {
            let _lang = vcFramework.getData('JAVA110-LANG');
            if (!_lang) {
                _lang = {
                    name: '简体中文',
                    lang: 'zh-cn'
                }
            }
            Vue.http.headers.common['JAVA110-LANG'] = _lang.lang;
            Vue.http.headers.common['APP-ID'] = '8000418004';
            Vue.http.headers.common['TRANSACTION-ID'] = vcFramework.uuid();
            Vue.http.headers.common['REQ-TIME'] = vcFramework.getDateYYYYMMDDHHMISS();
            Vue.http.headers.common['SIGN'] = '';
            vcFramework.loading('open');
            Vue.http.post('/callComponent/' + componentCode + "/" + componentMethod, param, options)
                .then(function(res) {
                    try {
                        let _header = res.headers.map;
                        //console.log('res', res);
                        if (vcFramework.notNull(_header['location'])) {
                            window.location.href = _header['location'];
                            return;
                        };
                        successCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                }, function(res) {
                    try {
                        if (res.status == 401 && res.headers.map["location"]) {
                            let _header = res.headers.map;
                            //console.log('res', res);
                            window.location.href = _header['location'];
                            return;
                        }
                        if (res.status == 404) {
                            window.location.href = '/user.html#/pages/frame/login';
                            return;
                        }
                        errorCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                });
        },
        get: function(componentCode, componentMethod, param, successCallback, errorCallback) {
            //加入缓存机制
            let _getPath = '/' + componentCode + '/' + componentMethod;
            if (vcFramework.constant.GET_CACHE_URL.includes(_getPath)) {
                let _cacheData = vcFramework.getData(_getPath);
                //浏览器缓存中能获取到
                if (_cacheData != null && _cacheData != undefined) {
                    successCallback(JSON.stringify(_cacheData), { status: 200 });
                    return;
                }
            }
            let _lang = vcFramework.getData('JAVA110-LANG');
            if (!_lang) {
                _lang = {
                    name: '简体中文',
                    lang: 'zh-cn'
                }
            }
            vcFramework.loading('open');
            Vue.http.headers.common['JAVA110-LANG'] = _lang.lang;
            Vue.http.headers.common['APP-ID'] = '8000418004';
            Vue.http.headers.common['TRANSACTION-ID'] = vcFramework.uuid();
            Vue.http.headers.common['REQ-TIME'] = vcFramework.getDateYYYYMMDDHHMISS();
            Vue.http.headers.common['SIGN'] = '';
            Vue.http.get('/callComponent/' + componentCode + "/" + componentMethod, param)
                .then(function(res) {
                    try {

                        successCallback(res.bodyText, res);
                        if (vcFramework.constant.GET_CACHE_URL.includes(_getPath) && res.status == 200) {
                            vcFramework.saveData('/nav/getUserInfo', JSON.parse(res.bodyText));
                        }
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                }, function(res) {
                    try {
                        if (res.status == 401 && res.headers.map["location"]) {
                            let _header = res.headers.map;
                            //console.log('res', res);
                            window.location.href = _header['location'];
                            return;

                        }
                        if (res.status == 404) {
                            window.location.href = '/user.html#/pages/frame/login';
                            return;
                        }
                        errorCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                });
        },
        apiPost: function(api, param, options, successCallback, errorCallback) {
            let _api = '';
            let _lang = vcFramework.getData('JAVA110-LANG');
            if (!_lang) {
                _lang = {
                    name: '简体中文',
                    lang: 'zh-cn'
                }
            }
            Vue.http.headers.common['JAVA110-LANG'] = _lang.lang;
            Vue.http.headers.common['APP-ID'] = '8000418004';
            Vue.http.headers.common['TRANSACTION-ID'] = vcFramework.uuid();
            Vue.http.headers.common['REQ-TIME'] = vcFramework.getDateYYYYMMDDHHMISS();
            Vue.http.headers.common['SIGN'] = '';
            if (api.indexOf('/') >= 0) {
                _api = '/app' + api;
            } else {
                _api = '/callComponent/' + api;
            }
            vcFramework.loading('open');
            Vue.http.post(_api, param, options)
                .then(function(res) {
                    vcFramework.loading('close');
                    try {
                        let _header = res.headers.map;
                        //console.log('res', res);
                        if (vcFramework.notNull(_header['location'])) {
                            window.location.href = _header['location'];
                            return;
                        };
                        successCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {}
                }, function(res) {
                    vcFramework.loading('close');
                    try {
                        if (res.status == 401 && res.headers.map["location"]) {
                            let _header = res.headers.map;
                            //console.log('res', res);
                            window.location.href = _header['location'];
                            return;
                        }
                        if (res.status == 404) {
                            window.location.href = '/user.html#/pages/frame/login';
                            return;
                        }
                        errorCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {}
                });
        },
        apiGet: function(api, param, successCallback, errorCallback) {
            //加入缓存机制
            let _getPath = '';
            if (api.indexOf('/') != 0) {
                _getPath = '/' + api;
            } else {
                _getPath = api;
            }
            if (vcFramework.constant.GET_CACHE_URL.includes(_getPath)) {
                let _cacheData = vcFramework.getData(_getPath);
                //浏览器缓存中能获取到
                if (_cacheData != null && _cacheData != undefined) {
                    successCallback(JSON.stringify(_cacheData), { status: 200 });
                    return;
                }
            }

            let _api = '';
            let _lang = vcFramework.getData('JAVA110-LANG');
            if (!_lang) {
                _lang = {
                    name: '简体中文',
                    lang: 'zh-cn'
                }
            }
            Vue.http.headers.common['JAVA110-LANG'] = _lang.lang;
            Vue.http.headers.common['APP-ID'] = '8000418004';
            Vue.http.headers.common['TRANSACTION-ID'] = vcFramework.uuid();
            Vue.http.headers.common['REQ-TIME'] = vcFramework.getDateYYYYMMDDHHMISS();
            Vue.http.headers.common['SIGN'] = '';
            Vue.http.headers.common['USER-ID'] = '-1';

            if (api.indexOf('/') >= 0) {
                _api = '/app' + api;
            } else {
                _api = '/callComponent/' + api;
            }
            if (vcFramework.hasOwnProperty('loading')) {
                vcFramework.loading('open');
            }

            Vue.http.get(_api, param)
                .then(function(res) {
                    try {

                        successCallback(res.bodyText, res);

                        if (vcFramework.constant.GET_CACHE_URL.includes(_getPath) && res.status == 200) {
                            vcFramework.saveData('/nav/getUserInfo', JSON.parse(res.bodyText));
                        }
                    } catch (e) {
                        console.error(e);
                    } finally {
                        if (vcFramework.hasOwnProperty('loading')) {
                            vcFramework.loading('close');
                        }
                    }
                }, function(res) {
                    try {
                        if (res.status == 401 && res.headers.map["location"]) {
                            let _header = res.headers.map;
                            //console.log('res', res);
                            window.location.href = _header['location'];
                            return;

                        }
                        if (res.status == 404) {
                            window.location.href = '/user.html#/pages/frame/login';
                            return;
                        }
                        errorCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                });
        },
        upload: function(componentCode, componentMethod, param, options, successCallback, errorCallback) {
            vcFramework.loading('open');
            Vue.http.post('/callComponent/upload/' + componentCode + "/" + componentMethod, param, options)
                .then(function(res) {
                    try {
                        successCallback(res.bodyText, res);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                }, function(error) {
                    try {
                        errorCallback(error.bodyText, error);
                    } catch (e) {
                        console.error(e);
                    } finally {
                        vcFramework.loading('close');
                    }
                });

        },

    };

    //let vmOptions = vcFramework.vmOptions;
    //继承方法,合并 _vmOptions 的数据到 vmOptions中
    vcFramework.extends = function(_vmOptions) {
        let vmOptions = vcFramework.vmOptions;
        if (typeof _vmOptions !== "object") {
            throw "_vmOptions is not Object";
        }
        //console.log('vmOptions',vmOptions);
        let nameSpace = DEFAULT_NAMESPACE;
        if (_vmOptions.hasOwnProperty("namespace")) {
            nameSpace = _vmOptions.namespace;
            vcFramework.namespace.push({
                namespace: _vmOptions.namespace,
            })
        }

        //处理 data 对象

        if (_vmOptions.hasOwnProperty('data')) {
            for (let dataAttr in _vmOptions.data) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.data[dataAttr] = _vmOptions.data[dataAttr];
                } else {

                    vmOptions.data[nameSpace + "_" + dataAttr] = _vmOptions.data[dataAttr];

                }
            }
        }
        //处理methods 对象
        if (_vmOptions.hasOwnProperty('methods')) {
            for (let methodAttr in _vmOptions.methods) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.methods[methodAttr] = _vmOptions.methods[methodAttr];
                } else {

                    vmOptions.methods[nameSpace + "_" + methodAttr] = _vmOptions.methods[methodAttr];
                }
            }
        }
        //处理methods 对象
        if (_vmOptions.hasOwnProperty('watch')) {
            for (let watchAttr in _vmOptions.watch) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.watch[watchAttr] = _vmOptions.watch[watchAttr];
                } else {

                    vmOptions.watch[nameSpace + "_" + watchAttr] = _vmOptions.watch[watchAttr];
                }
            }
        }
        //处理_initMethod 初始化执行函数
        if (_vmOptions.hasOwnProperty('_initMethod')) {
            vcFramework.initMethod.push(_vmOptions._initMethod);
        }
        //处理_initEvent
        if (_vmOptions.hasOwnProperty('_initEvent')) {
            vcFramework.initEvent.push(_vmOptions._initEvent);
        }

        //处理_initEvent_destroyedMethod
        if (_vmOptions.hasOwnProperty('_destroyedMethod')) {
            vcFramework.destroyedMethod.push(_vmOptions._destroyedMethod);
        }


    };
    //绑定跳转函数
    vcFramework.jumpToPage = function(url) {
        //判断 url 的模板是否 和当前url 模板一个
        console.log('jumpToPage', url);
        if (url.indexOf('#') < 0) {
            window.location.href = url;
            return;
        }
        //保存路由信息
        vcFramework.saveComponentToPageRoute();
        let _targetUrl = url.substring(0, url.indexOf('#'));
        if (location.pathname != _targetUrl) {
            window.location.href = url;
            return;
        }
        //刷新框架参数
        //refreshVcFramework();
        //修改锚点
        location.hash = url.substring(url.indexOf("#") + 1, url.length);
        //vcFramework.reBuilderVcTree();
    };

    refreshVcFramework = function() {
        $that.$destroy();
        let _vmOptions = {
            el: '#component',
            data: {},
            watch: {},
            methods: {},
            destroyed: function() {
                window.vcFramework.destroyedMethod.forEach(function(eventMethod) {
                    eventMethod();
                });
                //清理所有定时器

                window.vcFramework.timers.forEach(function(timer) {
                    clearInterval(timer);
                });

                _timers = [];
            }

        };
        vcFramework.vmOptions = _vmOptions;
        vcFramework.initMethod = [];
        vcFramework.initEvent = [];
        vcFramework.component = {};
        vcFramework.destroyedMethod = [];
        vcFramework.namespace = [];
    };
    //保存菜单
    vcFramework.setCurrentMenu = function(_menuId) {
        window.localStorage.setItem('hc_menuId', _menuId);
    };
    //获取菜单
    vcFramework.getCurrentMenu = function() {
        return window.localStorage.getItem('hc_menuId');
    };

    //保存用户菜单
    vcFramework.setMenus = function(_menus) {
        window.localStorage.setItem('hc_menus', JSON.stringify(_menus));
    };
    //获取用户菜单
    vcFramework.getMenus = function() {
        return JSON.parse(window.localStorage.getItem('hc_menus'));
    };

    //保存菜单状态
    vcFramework.setMenuState = function(_menuState) {
        window.localStorage.setItem('hc_menu_state', _menuState);
    };
    //获取菜单状态
    vcFramework.getMenuState = function() {
        return window.localStorage.getItem('hc_menu_state');
    };

    //保存用户菜单
    vcFramework.saveData = function(_key, _value) {
        window.localStorage.setItem(_key, JSON.stringify(_value));
    };
    //保存用户菜单
    vcFramework.removeData = function(_key) {
        Object.keys(localStorage).forEach(item => item.indexOf(_key) !== -1 ? localStorage.removeItem(item) : '');
    };
    //获取用户菜单
    vcFramework.getData = function(_key) {
        return JSON.parse(window.localStorage.getItem(_key));
    };

    //保存当前小区信息 _communityInfo : {"communityId":"123213","name":"测试小区"}
    vcFramework.setCurrentCommunity = function(_currentCommunityInfo) {
        window.localStorage.setItem('hc_currentCommunityInfo', JSON.stringify(_currentCommunityInfo));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vcFramework.getCurrentCommunity = function() {
        let _community = JSON.parse(window.localStorage.getItem('hc_currentCommunityInfo'));

        if (!_community) {
            return {
                communityId: '-1',
                communityName: '未知'
            };
        }

        return _community;
    };

    //保存当前小区信息 _communityInfos : [{"communityId":"123213","name":"测试小区"}]
    vcFramework.setCommunitys = function(_communityInfos) {
        window.localStorage.setItem('hc_communityInfos', JSON.stringify(_communityInfos));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vcFramework.getCommunitys = function() {
        return JSON.parse(window.localStorage.getItem('hc_communityInfos'));
    };

    //删除缓存数据
    vcFramework.clearCacheData = function() {
        window.localStorage.clear();
        window.sessionStorage.clear();
    };

    //将org 对象的属性值赋值给dst 属性名为一直的属性
    vcFramework.copyObject = function(org, dst) {

        if (!org || !dst) {
            return;
        }
        //for(key in Object.getOwnPropertyNames(dst)){
        for (let key in dst) {
            if (org.hasOwnProperty(key)) {
                dst[key] = org[key]
            }
        }
    };

    vcFramework.resetObject = function(org) {
        if (!org) {
            return;
        }
        //for(key in Object.getOwnPropertyNames(dst)){
        for (let key in org) {
            if (typeof(key) == "string") {
                org[key] = ''
            }
        }
    };
    //扩展 现有的对象 没有的属性扩充上去
    vcFramework.extendObject = function(org, dst) {
        for (let key in dst) {
            if (!org.hasOwnProperty(key)) {
                dst[key] = org[key]
            }
        }
    };
    vcFramework.getComponentCode = function() {
            let _componentUrl = location.hash;

            //判断是否为组件页面
            if (!vcFramework.notNull(_componentUrl)) {
                return "/";
            }

            if (_componentUrl.lastIndexOf('#') < 0) {
                return "/";
            }

            let endPos = _componentUrl.length;
            if (_componentUrl.indexOf('?') > -1) {
                endPos = _componentUrl.indexOf('?');
            }

            _componentUrl = _componentUrl.substring(_componentUrl.lastIndexOf('#') + 1, endPos);
            return _componentUrl;
        }
        //获取url参数
    vcFramework.getParam = function(_key) {
        //返回当前 URL 的查询部分（问号 ? 之后的部分）。
        let urlParameters = location.search;
        if (!vcFramework.notNull(urlParameters)) {
            urlParameters = location.hash;

            if (urlParameters.indexOf('?') != -1) {
                urlParameters = urlParameters.substring(urlParameters.indexOf('?'), urlParameters.length);
            }
        }
        //如果该求青中有请求的参数，则获取请求的参数，否则打印提示此请求没有请求的参数
        if (urlParameters.indexOf('?') != -1) {
            //获取请求参数的字符串
            let parameters = decodeURI(urlParameters.substr(1));
            //将请求的参数以&分割中字符串数组
            parameterArray = parameters.split('&');
            //循环遍历，将请求的参数封装到请求参数的对象之中
            for (let i = 0; i < parameterArray.length; i++) {
                if (_key == parameterArray[i].split('=')[0]) {
                    return parameterArray[i].split('=')[1];
                }
            }
        }
        return "";
    };
    //查询url
    vcFramework.getUrl = function() {
        //返回当前 URL 的查询部分（问号 ? 之后的部分）。
        let urlParameters = location.pathname;
        return urlParameters;
    };
    vcFramework.isBack = function() {
        let _back = vc.getData("JAVA110_IS_BACK");

        if (_back == null) {
            return false;
        }
        vc.removeData("JAVA110_IS_BACK");
        let beforeTime = _back;
        let _date = new Date();
        //10 秒内有效
        if (_date.getTime() - beforeTime < 10 * 1000) {
            return true;
        }
        return false;
    };
    vcFramework.getBack = function() {
        //window.location.href = document.referrer;
        let _date = new Date();
        vc.saveData("JAVA110_IS_BACK", _date.getTime());
        window.history.back(-1);
    }
    vcFramework.goBack = function() {
            //window.location.href = document.referrer;
            let _date = new Date();
            vc.saveData("JAVA110_IS_BACK", _date.getTime());
            window.history.back(-1);
        }
        //对象转get参数
    vcFramework.objToGetParam = function(obj) {
        let str = [];
        for (let p in obj)
            if (obj.hasOwnProperty(p)) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
        return str.join("&");
    };
    //空判断 true 为非空 false 为空
    vcFramework.notNull = function(_paramObj) {
        if (_paramObj == null || _paramObj == undefined || _paramObj.trim() == '') {
            return false;
        }
        return true;
    };
    vcFramework.isEmpty = function(_paramObj) {
        if (_paramObj == null || _paramObj == undefined) {
            return true;
        }
        return false;
    };
    //设置debug 模式
    vcFramework.setDebug = function(_param) {
        vcFramework.debug = _param;
    };
    //数据共享存放 主要为了组件间传递数据
    vcFramework.put = function(_key, _value) {
        vcFramework.map[_key] = _value;
    };
    //数据共享 获取 主要为了组件间传递数据
    vcFramework.get = function(_key) {
        return vcFramework.map[_key];
    };

    vcFramework.getDict = function(_name, _type, _callFun) {
        let param = {
            params: {
                name: _name,
                type: _type
            }
        };

        //发送get请求
        vcFramework.http.get('core', 'list', param,
            function(json, res) {
                if (res.status == 200) {
                    let _dictInfo = JSON.parse(json);
                    _callFun(_dictInfo);
                    return;
                }
            },
            function(errInfo, error) {
                console.log('请求失败处理');
            });
    }

    vcFramework.getAttrSpec = function(_tableName, _callFun, _domain) {
        let param = {
            params: {
                tableName: _tableName,
                page: 1,
                row: 100,
                domain: _domain
            }
        };

        //发送get请求
        vcFramework.http.apiGet('/attrSpec/queryAttrSpec', param,
            function(json, res) {
                let _attrSpecInfo = JSON.parse(json);

                if (_attrSpecInfo.code == 0) {
                    _callFun(_attrSpecInfo.data);
                    return;
                }
            },
            function(errInfo, error) {
                console.log('请求失败处理');
            });
    }


    vcFramework.getAttrValue = function(_specCd, _callFun, _domain) {
        let param = {
            params: {
                specCd: _specCd,
                page: 1,
                row: 100,
                domain: _domain
            }
        };

        //发送get请求
        vcFramework.http.apiGet('/attrValue/queryAttrValue', param,
            function(json, res) {
                let _attrSpecInfo = JSON.parse(json);

                if (_attrSpecInfo.code == 0) {
                    _callFun(_attrSpecInfo.data);
                    return;
                }
            },
            function(errInfo, error) {
                console.log('请求失败处理');
            });
    }

    vcFramework.refreshSystemInfo = function() {
        let param = {
            params: {
                page: 1,
                row: 1,
            }
        };

        //发送get请求

    }




})(window.vcFramework);

/**
 vc 定时器处理
 **/
(function(w, vcFramework) {

    /**
     创建定时器
     **/
    vcFramework.createTimer = function(func, sec) {
        let _timer = w.setInterval(func, sec);
        vcFramework.timers.push(_timer); //这里将所有的定时器保存起来，页面退出时清理

        return _timer;
    };
    //清理定时器
    vcFramework.clearTimer = function(timer) {
        clearInterval(timer);
    }


})(window, window.vcFramework);

/**
 * vcFramework.toast("");
 时间处理工具类
 **/
(function(vcFramework) {
    function add0(m) {
        return m < 10 ? '0' + m : m
    }



    vcFramework.dateTimeFormat = function(shijianchuo) {
        //shijianchuo是整数，否则要parseInt转换
        let time = new Date(parseInt(shijianchuo));
        let y = time.getFullYear();
        let m = time.getMonth() + 1;
        let d = time.getDate();
        let h = time.getHours();
        let mm = time.getMinutes();
        let s = time.getSeconds();
        return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
    }

    vcFramework.dateFormat = function(_time) {
        let _date = new Date(_time);
        let y = _date.getFullYear();
        let m = _date.getMonth() + 1;
        let d = _date.getDate();
        return y + '-' + add0(m) + '-' + add0(d);
    }

    vcFramework.dateSubOneDay = function(_startTime, _endTime, feeFlag) {
        if (!_endTime || _endTime == '-') {
            return _endTime
        }
        let dateTime = new Date(_endTime);
        let startTime = new Date(_startTime);
        //如果开始时间是31日 结束时间是30日 不做处理
        let _startTimeLastDay = startTime.getDate();
        let _endTimeLastDay = dateTime.getDate();
        if (_startTimeLastDay == 31 && _endTimeLastDay == 30) {
            return vcFramework.dateFormat(dateTime);
        }

        //2月份特殊处理
        let _endTimeMonth = dateTime.getMonth();
        if (_endTimeMonth == 1 && _endTimeLastDay > 26 && _startTimeLastDay > 26) {
            return vcFramework.dateFormat(dateTime);
        }

        if (feeFlag != "2006012") {
            dateTime = dateTime.setDate(dateTime.getDate() - 1);
        }
        dateTime = vcFramework.dateFormat(dateTime)
        return dateTime;
    }

    vcFramework.dateSub = function(dateTime, feeFlag) {
        if (!dateTime || dateTime == '-') {
            return dateTime
        }
        console.log("feeFlag:" + feeFlag);
        dateTime = new Date(dateTime);
        if (feeFlag != "2006012") {
            dateTime = dateTime.setDate(dateTime.getDate() - 1);
        }
        dateTime = vcFramework.dateFormat(dateTime)
        return dateTime;
    }
    vcFramework.dateAdd = function(dateTime) {
        if (!dateTime || dateTime == '-') {
            return dateTime
        }
        dateTime = new Date(dateTime);
        dateTime = dateTime.setDate(dateTime.getDate() + 1);
        dateTime = vcFramework.dateFormat(dateTime)
        return dateTime;
    }


    vcFramework.getDateYYYYMMDDHHMISS = function() {
        let date = new Date();
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();

        if (month < 10) {
            month = '0' + month;
        }

        if (day < 10) {
            day = '0' + day;
        }

        if (hour < 10) {
            hour = '0' + hour;
        }

        if (minute < 10) {
            minute = '0' + minute;
        }

        if (second < 10) {
            second = '0' + second;
        }

        return year + "" + month + "" + day + "" + hour + "" + minute + "" + second;
    };

    vcFramework.initDateTime = function(_dateStr, _callBack) {
        $('.' + _dateStr).datetimepicker({
            language: 'zh-CN',
            fontAwesome: 'fa',
            format: 'yyyy-mm-dd hh:ii:ss',
            initTime: true,
            initialDate: new Date(),
            autoClose: 1,
            todayBtn: true
        });
        $('.' + _dateStr).datetimepicker()
            .on('changeDate', function(ev) {
                var value = $('.' + _dateStr).val();
                //vc.component.addFeeConfigInfo.startTime = value;
                _callBack(value);
            });
    }

    vcFramework.initDate = function(_dateStr, _callBack) {
        $('.' + _dateStr).datetimepicker({
            language: 'zh-CN',
            minView: 'month',
            fontAwesome: 'fa',
            format: 'yyyy-mm-dd',
            initTime: true,
            initialDate: new Date(),
            autoClose: 1,
            todayBtn: true

        });
        $('.' + _dateStr).datetimepicker()
            .on('changeDate', function(ev) {
                let value = $('.' + _dateStr).val();
                //vc.component.addFeeConfigInfo.startTime = value;
                _callBack(value);
            });
    }

    vcFramework.initHourMinute = function(_dateStr, _callBack) {
        $('.' + _dateStr).datetimepicker({
            language: 'zh-CN',
            fontAwesome: 'fa',
            format: 'hh:ii',
            initTime: true,
            startView: 'day',
            autoClose: 1,
            todayBtn: true

        });
        $('.' + _dateStr).datetimepicker()
            .on('changeDate', function(ev) {
                var value = $('.' + _dateStr).val();
                //vc.component.addFeeConfigInfo.startTime = value;
                _callBack(value);
            });
    }

    vcFramework.initDateMonth = function(_dateStr, _callBack) {
        $('.' + _dateStr).datetimepicker({
            language: 'zh-CN',
            fontAwesome: 'fa',
            format: 'yyyy-mm',
            initTime: true,
            startView: 3,
            minView: 3,
            initialDate: new Date(),
            autoClose: 1,
            todayBtn: true
        });
        $('.' + _dateStr).datetimepicker()
            .on('changeDate', function(ev) {
                let value = $('.' + _dateStr).val();
                //vc.component.addFeeConfigInfo.startTime = value;
                _callBack(value);
            });
    }

    daysInMonth = function(year, month) {
        if (month == 1) {
            if (year % 4 == 0 && year % 100 != 0)
                return 29;
            else
                return 28;
        } else if ((month <= 6 && month % 2 == 0) || (month = 6 && month % 2 == 1))
            return 31;
        else
            return 30;
    }

    vcFramework.unum = function(_money) {
        return parseFloat(_money) * -1;
    }

    vcFramework.addMonth = function(_date, _month) {
        let y = _date.getFullYear();
        let m = _date.getMonth();
        let nextY = y;
        let nextM = m;
        //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
        if ((m + _month) > 11) {
            nextY = y + 1;
            nextM = parseInt(m + _month) - 12;
        } else {
            nextM = m + _month
        }
        let daysInNextMonth = daysInMonth(nextY, nextM);
        let day = _date.getDate();
        if (day > daysInNextMonth) {
            day = daysInNextMonth;
        }
        let _newDate = new Date(nextY, nextM, day)
        return _newDate.getFullYear() + '-' + (_newDate.getMonth() + 1) + '-' + _newDate.getDate() + " " + _date.getHours() + ":" + _date.getMinutes() + ":" + _date.getSeconds();
    };
    vcFramework.addMonthDate = function(_date, _month) {
        let y = _date.getFullYear();
        let m = _date.getMonth();
        let nextY = y;
        let nextM = m;
        //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
        if ((m + _month) > 11) {
            nextY = y + 1;
            nextM = parseInt(m + _month) - 12;
        } else {
            nextM = m + _month
        }
        let daysInNextMonth = daysInMonth(nextY, nextM);
        let day = _date.getDate();
        if (day > daysInNextMonth) {
            day = daysInNextMonth;
        }
        let _newDate = new Date(nextY, nextM, day)
        return _newDate.getFullYear() + '-' + add0(_newDate.getMonth() + 1) + '-' + add0(_newDate.getDate());
    };

    vcFramework.popover = function(_className) {
        $("." + _className).mouseover(() => {
            $("." + _className).popover('show');
        })
        $("." + _className).mouseleave(() => {
            $("." + _className).popover('hide');
        })
    }
})(window.vcFramework);

(function(vcFramework) {
    vcFramework.propTypes = {
        string: "string", //字符串类型
        array: "array",
        object: "object",
        number: "number"
    }
})(window.vcFramework);

/**
 toast
 **/
(function(vcFramework) {
    vcFramework.toast = function Toast(msg, duration) {
        duration = isNaN(duration) ? 3000 : duration;
        let m = document.createElement('div');
        m.innerHTML = msg;
        m.style.cssText = "max-width:60%;min-width: 150px;padding:0 14px;height: 40px;color: rgb(255, 255, 255);line-height: 40px;text-align: center;border-radius: 4px;position: fixed;top: 30%;left: 50%;transform: translate(-50%, -50%);z-index: 999999;background: rgba(0, 0, 0,.7);font-size: 16px;";
        document.body.appendChild(m);
        setTimeout(function() {
            let d = 0.5;
            m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
            m.style.opacity = '0';
            setTimeout(function() {
                document.body.removeChild(m)
            }, d * 1000);
        }, duration);
    }
})(window.vcFramework);

/**
 isNumber
 **/
(function(vcFramework) {
    vcFramework.isNumber = function(val) {

        var regPos = /^\d+(\.\d+)?$/; //非负浮点数
        var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
        if (regPos.test(val) || regNeg.test(val)) {
            return true;
        } else {
            return false;
        }
    }
})(window.vcFramework);

/**
 toast
 **/
(function(vcFramework) {
    vcFramework.urlToBase64 = function urlToBase64(_url, _callFun) {
        let imgData;
        let reader = new FileReader();
        getImageBlob(_url, function(blob) {
            reader.readAsDataURL(blob);
        });
        reader.onload = function(e) {
            imgData = e.target.result;
            _callFun(imgData);
        };

        function getImageBlob(_url, cb) {
            let xhr = new XMLHttpRequest();
            xhr.open("get", _url, true);
            xhr.responseType = "blob";
            xhr.onload = function() {
                if (this.status == 200) {
                    if (cb) cb(this.response);
                }
            };
            xhr.send();
        }
    }
})(window.vcFramework);

/***
 * vc component 0.1 版本代码合并过来（end）-----------------------------------------------------------------------
 */
/**
 初始化vue 对象
 @param vc vue component对象
 @param vmOptions Vue参数
 **/
(function(vcFramework) {
    vcFramework.initVue = function() {
        let vmOptions = vcFramework.vmOptions;
        //console.log("vmOptions:", vmOptions);
        vcFramework.vue = new Vue(vmOptions);
        vcFramework.component = vcFramework.vue;
        //方便二次开发
        window.$that = vcFramework.vue;
        //发布vue 创建完成 事件
        document.dispatchEvent(vcFramework.vcFrameworkEvent);
    }
})(window.vcFramework);





/**
 vc监听事件
 **/
(function(vcFramework) {
    /**
     事件监听
     **/
    vcFramework.on = function() {
        let _namespace = "";
        let _componentName = "";
        let _value = "";
        let _callback = undefined;
        if (arguments.length == 4) {
            _namespace = arguments[0];
            _componentName = arguments[1];
            _value = arguments[2];
            _callback = arguments[3];
        } else if (arguments.length == 3) {
            _componentName = arguments[0];
            _value = arguments[1];
            _callback = arguments[2];
        } else {
            console.error("执行on 异常，vcFramework.on 参数只能是3个 或4个");
            return;
        }
        if (vcFramework.notNull(_namespace)) {
            vcFramework.vue.$on(_namespace + "_" + _componentName + '_' + _value,
                function(param) {
                    if (vcFramework.debug) {
                        console.log("监听ON事件", _namespace, _componentName, _value, param);
                    }
                    _callback(param);
                }
            );
            return;
        }
        vcFramework.vue.$on(_componentName + '_' + _value,
            function(param) {
                if (vcFramework.debug) {
                    console.log("监听ON事件", _componentName, _value, param);
                }
                _callback(param);
            }
        );
    };

    /**
     事件触发
     **/
    vcFramework.emit = function() {
        let _namespace = "";
        let _componentName = "";
        let _value = "";
        let _param = undefined;
        if (arguments.length == 4) {
            _namespace = arguments[0];
            _componentName = arguments[1];
            _value = arguments[2];
            _param = arguments[3];
        } else if (arguments.length == 3) {
            _componentName = arguments[0];
            _value = arguments[1];
            _param = arguments[2];
        } else {
            console.error("执行on 异常，vcFramework.on 参数只能是3个 或4个");
            return;
        }
        if (vcFramework.debug) {
            console.log("监听emit事件", _namespace, _componentName, _value, _param);
        }
        if (vcFramework.notNull(_namespace)) {
            vcFramework.vue.$emit(_namespace + "_" + _componentName + '_' + _value, _param);
            return;
        }
        vcFramework.vue.$emit(_componentName + '_' + _value, _param);
    };
})(window.vcFramework);

/**
 * vue对象 执行初始化方法
 */
(function(vcFramework) {
    vcFramework.initVcComponent = function() {
        vcFramework.initEvent.forEach(function(eventMethod) {
            eventMethod();
        });
        vcFramework.initMethod.forEach(function(callback) {
            callback();
        });
        vcFramework.namespace.forEach(function(_param) {
            vcFramework[_param.namespace] = vcFramework.vue[_param.namespace];
        });
    }
})(window.vcFramework);
/**
 * 锚点变化监听
 */
(function(vcFramework) {
    window.addEventListener("hashchange", function(e) {
        let _componentUrl = location.hash;
        //判断是否为组件页面
        if (!vcFramework.notNull(_componentUrl)) {
            return;
        }
        //获取参数
        let _tab = vc.getParam('tab')

        if (_tab) {
            vcFramework.setTabToLocal({
                url: _componentUrl,
                name: _tab
            });
        }
        refreshVcFramework();
        vcFramework.reBuilderVcTree();
    }, false);
})(window.vcFramework);

/**
 * vcFramwork init
 * 框架开始初始化
 */
(function(vcFramework) {

    vcFramework.builderVcTree();
    //vcFramework.refreshSystemInfo();


})(window.vcFramework);

/**
 vc 校验 工具类 -method
 (1)、required:true               必输字段
 (2)、remote:"remote-valid.jsp"   使用ajax方法调用remote-valid.jsp验证输入值
 (3)、email:true                  必须输入正确格式的电子邮件
 (4)、url:true                    必须输入正确格式的网址
 (5)、date:true                   必须输入正确格式的日期，日期校验ie6出错，慎用
 (6)、dateISO:true                必须输入正确格式的日期(ISO)，例如：2009-06-23，1998/01/22 只验证格式，不验证有效性
 (7)、number:true                 必须输入合法的数字(负数，小数)
 (8)、digits:true                 必须输入整数
 (9)、creditcard:true             必须输入合法的信用卡号
 (10)、equalTo:"#password"        输入值必须和#password相同
 (11)、accept:                    输入拥有合法后缀名的字符串（上传文件的后缀）
 (12)、maxlength:5                输入长度最多是5的字符串(汉字算一个字符)
 (13)、minlength:10               输入长度最小是10的字符串(汉字算一个字符)
 (14)、rangelength:[5,10]         输入长度必须介于 5 和 10 之间的字符串")(汉字算一个字符)
 (15)、range:[5,10]               输入值必须介于 5 和 10 之间
 (16)、max:5                      输入值不能大于5
 (17)、min:10                     输入值不能小于10
 **/
(function(vcFramework) {
    let validate = {
        state: true,
        errInfo: '',
        setState: function(_state, _errInfo) {
            this.state = _state;
            if (!this.state) {
                this.errInfo = _errInfo
                throw "校验失败:" + _errInfo;
            }
        },

        /**
         校验手机号
         **/
        phone: function(text) {
            let regPhone = /^0?1[3|4|5|6|7|8|9][0-9]\d{8}$/;
            return regPhone.test(text);
        },
        /**
         校验邮箱
         **/
        email: function(text) {
            let regEmail = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
            return regEmail.test(text);
        },
        /**
         * 必填
         * @param {参数} text
         */
        required: function(text) {
            if (text == undefined || text == null || text == "") {
                return false;
            }
            return true;
        },
        /**
         * 校验长度
         * @param {校验文本} text
         * @param {最小长度} minLength
         * @param {最大长度} maxLength
         */
        maxin: function(text, minLength, maxLength) {
            if (text.length < minLength || text.length > maxLength) {
                return false;
            }
            return true;
        },
        /**
         * 校验长度
         * @param {校验文本} text
         * @param {最大长度} maxLength
         */
        maxLength: function(text, maxLength) {
            if (text.length > maxLength) {
                return false;
            }
            return true;
        },
        /**
         * 校验最小长度
         * @param {校验文本} text
         * @param {最小长度} minLength
         */
        minLength: function(text, minLength) {
            if (text.length < minLength) {
                return false;
            }
            return true;
        },
        /**
         * 全是数字
         * @param {校验文本} text
         */
        num: function(text) {
            if (text == null || text == undefined) {
                return true;
            }
            let regNum = /^[0-9][0-9]*$/;
            return regNum.test(text);
        },
        date: function(str) {
            if (str == null || str == undefined) {
                return true;
            }
            let regDate = /^(\d{4})-(\d{2})-(\d{2})$/;
            return regDate.test(str);
        },
        dateTime: function(str) {
            if (str == null || str == undefined) {
                return true;
            }
            let reDateTime = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
            return reDateTime.test(str);
        },
        /**
         金额校验
         **/
        money: function(text) {
            if (text == null || text == undefined) {
                return true;
            }
            let regMoney = /^\d+\.?\d{0,2}$/;
            return regMoney.test(text);
        },
        /**
         系数校验
         **/
        moneyModulus: function(text) {
            if (text == null || text == undefined) {
                return true;
            }
            let regMoney = /^\d+\.?\d{0,4}$/;
            return regMoney.test(text);
        },
        idCard: function(num) {
            if (num == null || num == undefined || num == '') {
                return true;
            }
            num = num.toUpperCase();
            //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。
            if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
                return false;
            }
            return true;
        },
        /**
            校验最小值
        **/
        min: function(text, minVal) {
            if (parseFloat(text) >= parseFloat(minVal)) {
                return true;
            }
            return false;
        },
        /**
            校验最大值
        **/
        max: function(text, maxVal) {
            if (parseFloat(text) <= parseFloat(maxVal)) {
                return true;
            }
            return false;
        },

    };
    vc.validate = validate;
})(window.vcFramework);

/**
 * 校验 -core
 */
(function(validate) {

    /**
     * 根据配置校验
     *
     * eg:
     * dataObj:
     * {
     *      name:"Kevin",
     *      age:"19",
     *      emailInfo:{
     *          email:"928255095@qq.com"
     *      }
     * }
     *
     * dataConfig:
     * {
     *      "name":[
                    {
                       limit:"required",
                       param:"",
                       errInfo:'用户名为必填'
                    },
                    {
                        limit:"maxin",
                       param:"1,10",
                       errInfo:'用户名必须为1到10个字之间'
                    }]
     * }
     *
     */
    validate.validate = function(dataObj, dataConfig) {
        try {
            // 循环配置（每个字段）
            for (let key in dataConfig) {
                //配置信息
                let tmpDataConfigValue = dataConfig[key];
                //对key进行处理
                let keys = key.split(".");
                console.log("keys :", keys);
                let tmpDataObj = dataObj;
                //根据配置获取 数据值
                keys.forEach(function(tmpKey) {
                    console.log('tmpDataObj:', tmpDataObj);
                    tmpDataObj = tmpDataObj[tmpKey]
                });
                //                for(let tmpKey in keys){
                //                    console.log('tmpDataObj:',tmpDataObj);
                //                    tmpDataObj = tmpDataObj[tmpKey]
                //                }

                tmpDataConfigValue.forEach(function(configObj) {
                    if (configObj.limit == "required") {
                        validate.setState(validate.required(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'phone') {
                        validate.setState(validate.phone(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'email') {
                        validate.setState(validate.email(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'maxin') {
                        let tmpParam = configObj.param.split(",")
                        validate.setState(validate.maxin(tmpDataObj, tmpParam[0], tmpParam[1]), configObj.errInfo);
                    }
                    if (configObj.limit == 'maxLength') {
                        validate.setState(validate.maxLength(tmpDataObj, configObj.param), configObj.errInfo);
                    }
                    if (configObj.limit == 'minLength') {
                        validate.setState(validate.minLength(tmpDataObj, configObj.param), configObj.errInfo);
                    }
                    if (configObj.limit == 'num') {
                        validate.setState(validate.num(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'date') {
                        validate.setState(validate.date(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'dateTime') {
                        validate.setState(validate.dateTime(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'money') {
                        validate.setState(validate.money(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'idCard') {
                        validate.setState(validate.idCard(tmpDataObj), configObj.errInfo);
                    }
                    if (configObj.limit == 'min') {
                        validate.setState(validate.min(tmpDataObj, configObj.param), configObj.errInfo);
                    }
                    if (configObj.limit == 'max') {
                        validate.setState(validate.max(tmpDataObj, configObj.param), configObj.errInfo);
                    }
                });

            }
        } catch (error) {
            console.log("数据校验失败", validate.state, validate.errInfo, error);
            return false;
        }
        return true;
    }
})(window.vcFramework.validate);

/**
 对 validate 进行二次封装
 **/
(function(vcFramework) {
    vcFramework.check = function(dataObj, dataConfig) {
        return vcFramework.validate.validate(dataObj, dataConfig);
    }
})(window.vcFramework);

/**
 * 监听div 大小
 */
(function(vcFramework) {
    vcFramework.eleResize = {
        _handleResize: function(e) {
            let ele = e.target || e.srcElement;
            let trigger = ele.__resizeTrigger__;
            if (trigger) {
                let handlers = trigger.__z_resizeListeners;
                if (handlers) {
                    let size = handlers.length;
                    for (let i = 0; i < size; i++) {
                        let h = handlers[i];
                        let handler = h.handler;
                        let context = h.context;
                        handler.apply(context, [e]);
                    }
                }
            }
        },
        _removeHandler: function(ele, handler, context) {
            let handlers = ele.__z_resizeListeners;
            if (handlers) {
                let size = handlers.length;
                for (let i = 0; i < size; i++) {
                    let h = handlers[i];
                    if (h.handler === handler && h.context === context) {
                        handlers.splice(i, 1);
                        return;
                    }
                }
            }
        },
        _createResizeTrigger: function(ele) {
            let obj = document.createElement('object');
            obj.setAttribute('style',
                'display: block; position: absolute; top: 0; left: 0; height: 100%; width: 100%; overflow: hidden;opacity: 0; pointer-events: none; z-index: -1;');
            obj.onload = vcFramework.eleResize._handleObjectLoad;
            obj.type = 'text/html';
            ele.appendChild(obj);
            obj.data = 'about:blank';
            return obj;
        },
        _handleObjectLoad: function(evt) {
            this.contentDocument.defaultView.__resizeTrigger__ = this.__resizeElement__;
            this.contentDocument.defaultView.addEventListener('resize', vcFramework.eleResize._handleResize);
        }
    };
    if (document.attachEvent) { //ie9-10
        vcFramework.eleResize.on = function(ele, handler, context) {
            let handlers = ele.__z_resizeListeners;
            if (!handlers) {
                handlers = [];
                ele.__z_resizeListeners = handlers;
                ele.__resizeTrigger__ = ele;
                ele.attachEvent('onresize', EleResize._handleResize);
            }
            handlers.push({
                handler: handler,
                context: context
            });
        };
        vcFramework.eleResize.off = function(ele, handler, context) {
            let handlers = ele.__z_resizeListeners;
            if (handlers) {
                EleResize._removeHandler(ele, handler, context);
                if (handlers.length === 0) {
                    ele.detachEvent('onresize', EleResize._handleResize);
                    delete ele.__z_resizeListeners;
                }
            }
        }
    } else {
        vcFramework.eleResize.on = function(ele, handler, context) {
            let handlers = ele.__z_resizeListeners;
            if (!handlers) {
                handlers = [];
                ele.__z_resizeListeners = handlers;
                if (getComputedStyle(ele, null).position === 'static') {
                    ele.style.position = 'relative';
                }
                let obj = vcFramework.eleResize._createResizeTrigger(ele);
                ele.__resizeTrigger__ = obj;
                obj.__resizeElement__ = ele;
            }
            handlers.push({
                handler: handler,
                context: context
            });
        };
        vcFramework.eleResize.off = function(ele, handler, context) {
            let handlers = ele.__z_resizeListeners;
            if (handlers) {
                vcFramework.eleResize._removeHandler(ele, handler, context);
                if (handlers.length === 0) {
                    let trigger = ele.__resizeTrigger__;
                    if (trigger) {
                        trigger.contentDocument.defaultView.removeEventListener('resize', EleResize._handleResize);
                        ele.removeChild(trigger);
                        delete ele.__resizeTrigger__;
                    }
                    delete ele.__z_resizeListeners;
                }
            }
        }
    }
})(window.vcFramework);

//全屏处理 这个后面可以关掉
(function(vcFramework) {
    vcFramework._fix_height = (_targetDiv) => {
        //只要窗口高度发生变化，就会进入这里面，在这里就可以写，回到聊天最底部的逻辑
        let _vcPageHeight = document.getElementsByClassName('vc-page-height')[0];
        //浏览器可见高度
        let _minHeight = document.documentElement.clientHeight;
        let _scollHeight = _targetDiv.scrollHeight;
        if (_scollHeight < _minHeight) {
            _scollHeight = _minHeight
        }
        _vcPageHeight.style.minHeight = _scollHeight + 'px';
        //console.log('是否设置高度', _vcPageHeight.style.minHeight);
    }
})(window.vcFramework);

/**
 * 权限处理
 */
(function(vcFramework) {
    let _staffPrivilege = vc.getData('hc_staff_privilege');
    if (_staffPrivilege == null) {
        _staffPrivilege = [];
    }
    vcFramework.hasPrivilege = (_privalege) => {
        //只要窗口高度发生变化，就会进入这里面，在这里就可以写，回到聊天最底部的逻辑
        return _staffPrivilege.includes(_privalege);
    }
})(window.vcFramework);


//图片压缩处理

(function(vcFramework) {
    vcFramework.translate = function(imgSrc, callback) {
        var img = new Image();
        img.src = imgSrc;
        img.onload = function() {
            var that = this;
            var h = that.height;
            // 默认按比例压缩
            var w = that.width;
            if (h > 1080 || w > 1080) {
                let _rate = 0;
                if (h > w) {
                    _rate = h / 1080;
                    h = 1080;
                    w = Math.floor(w / _rate);
                } else {
                    _rate = w / 1080;
                    w = 1080;
                    h = Math.floor(h / _rate);
                }
            }
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');
            var anw = document.createAttribute("width");
            anw.nodeValue = w;
            var anh = document.createAttribute("height");
            anh.nodeValue = h;
            canvas.setAttributeNode(anw);
            canvas.setAttributeNode(anh);
            ctx.drawImage(that, 0, 0, w, h);
            //压缩比例
            var quality = 0.3;
            var base64 = canvas.toDataURL('image/jpeg', quality);
            canvas = null;
            callback(base64);

        }
    }
})(window.vcFramework);

/**
 * 水印处理
 */

(function(vcFramework) {
    vcFramework.watermark = function(settings) {
        //默认设置
        var defaultSettings = {
            watermark_txt: "text",
            watermark_x: 20, //水印起始位置x轴坐标
            watermark_y: 20, //水印起始位置Y轴坐标
            watermark_rows: 100, //水印行数
            watermark_cols: 20, //水印列数
            watermark_x_space: 10, //水印x轴间隔
            watermark_y_space: 10, //水印y轴间隔
            watermark_color: '#aaa', //水印字体颜色
            watermark_alpha: 0.3, //水印透明度
            watermark_fontsize: '15px', //水印字体大小
            watermark_font: '微软雅黑', //水印字体
            watermark_width: 150, //水印宽度
            watermark_height: 80, //水印长度
            watermark_angle: 15 //水印倾斜度数
        };
        //采用配置项替换默认值，作用类似jquery.extend
        if (arguments.length === 1 && typeof arguments[0] === "object") {
            var src = arguments[0] || {};
            for (key in src) {
                if (src[key] && defaultSettings[key] && src[key] === defaultSettings[key])
                    continue;
                else if (src[key])
                    defaultSettings[key] = src[key];
            }
        }

        let oTemp = document.createDocumentFragment();

        //获取页面最大宽度
        let page_width = Math.max(document.body.scrollWidth, document.body.clientWidth);
        let cutWidth = page_width * 0.0150;
        page_width = page_width - cutWidth;
        //获取页面最大高度
        let page_height = Math.max(document.body.scrollHeight - 80, document.body.clientHeight - 40);
        // var page_height = document.body.scrollHeight+document.body.scrollTop;
        //如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
        if (defaultSettings.watermark_cols == 0 || (parseInt(defaultSettings.watermark_x + defaultSettings.watermark_width * defaultSettings.watermark_cols + defaultSettings.watermark_x_space * (defaultSettings.watermark_cols - 1)) > page_width)) {
            defaultSettings.watermark_cols = parseInt((page_width - defaultSettings.watermark_x + defaultSettings.watermark_x_space) / (defaultSettings.watermark_width + defaultSettings.watermark_x_space));
            defaultSettings.watermark_x_space = parseInt((page_width - defaultSettings.watermark_x - defaultSettings.watermark_width * defaultSettings.watermark_cols) / (defaultSettings.watermark_cols - 1));
        }
        //如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
        if (defaultSettings.watermark_rows == 0 || (parseInt(defaultSettings.watermark_y + defaultSettings.watermark_height * defaultSettings.watermark_rows + defaultSettings.watermark_y_space * (defaultSettings.watermark_rows - 1)) > page_height)) {
            defaultSettings.watermark_rows = parseInt((defaultSettings.watermark_y_space + page_height - defaultSettings.watermark_y) / (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
            defaultSettings.watermark_y_space = parseInt(((page_height - defaultSettings.watermark_y) - defaultSettings.watermark_height * defaultSettings.watermark_rows) / (defaultSettings.watermark_rows - 1));
        }
        let x;
        let y;
        for (let i = 0; i < defaultSettings.watermark_rows; i++) {
            y = defaultSettings.watermark_y + (defaultSettings.watermark_y_space + defaultSettings.watermark_height) * i;
            for (let j = 0; j < defaultSettings.watermark_cols; j++) {
                x = defaultSettings.watermark_x + (defaultSettings.watermark_width + defaultSettings.watermark_x_space) * j;
                var mask_div = document.createElement('div');
                mask_div.id = 'mask_div' + i + j;
                mask_div.className = 'mask_div';
                mask_div.appendChild(document.createTextNode(defaultSettings.watermark_txt));
                //设置水印div倾斜显示
                mask_div.style.webkitTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.MozTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.msTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.OTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.transform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.visibility = "";
                mask_div.style.position = "fixed";
                mask_div.style.left = x + 'px';
                mask_div.style.top = y + 'px';
                mask_div.style.overflow = "hidden";
                mask_div.style.zIndex = "9999";
                mask_div.style.pointerEvents = 'none'; //pointer-events:none  让水印不遮挡页面的点击事件
                //mask_div.style.border="solid #eee 1px";
                mask_div.style.opacity = defaultSettings.watermark_alpha;
                mask_div.style.fontSize = defaultSettings.watermark_fontsize;
                mask_div.style.fontFamily = defaultSettings.watermark_font;
                mask_div.style.color = defaultSettings.watermark_color;
                mask_div.style.textAlign = "center";
                mask_div.style.width = defaultSettings.watermark_width + 'px';
                mask_div.style.height = defaultSettings.watermark_height + 'px';
                mask_div.style.display = "block";
                //交叉网格显示
                if ((i % 2 == 0) && (j % 2 == 0)) {
                    oTemp.appendChild(mask_div);
                }
                if ((i % 2 == 1) && (j % 2 == 1)) {
                    oTemp.appendChild(mask_div);
                }
            };
        };
        document.body.appendChild(oTemp);
    }
})(window.vcFramework);

//解决 toFixed bug 问题
(function(vcFramework) {
    Number.prototype.toFixed = function(d) {
        var s = this + "";
        if (!d) d = 0;
        if (s.indexOf(".") == -1) s += ".";
        s += new Array(d + 1).join("0");
        if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0," + (d + 1) + "})?)\\d*$").test(s)) {
            var s = "0" + RegExp.$2,
                pm = RegExp.$1,
                a = RegExp.$3.length,
                b = true;
            if (a == d + 2) {
                a = s.match(/\d/g);
                if (parseInt(a[a.length - 1]) > 4) {
                    for (var i = a.length - 2; i >= 0; i--) {
                        a[i] = parseInt(a[i]) + 1;
                        if (a[i] == 10) {
                            a[i] = 0;
                            b = i != 1;
                        } else break;
                    }
                }
                s = a.join("").replace(new RegExp("(\\d+)(\\d{" + d + "})\\d$"), "$1.$2");


            }
            if (b) s = s.substr(1);
            return (pm + s).replace(/\.$/, "");
        }
        return this + "";
    }


})(window.vcFramework);

(function(vcFramework) {

    vcFramework.getPageRouteFromLocal = function() {
        let routesStr = window.localStorage.getItem('vcPageRoute');

        let routes = [];

        if (!routesStr) {
            window.localStorage.setItem('vcPageRoute', JSON.stringify(routes))
        } else {
            routes = JSON.parse(routesStr);
        }

        return routes;
    }

    vcFramework.setPageRouteToLocal = function(_obj) {
        let routes = vcFramework.getPageRouteFromLocal();

        //判断是否已经有 如果有则删除
        let loction = 0;
        for (let routeIndex = 0; routeIndex < routes.length; routeIndex++) {
            _tmpRoute = routes[routeIndex];
            if (!_tmpRoute.pagePath || _tmpRoute.pagePath != _obj.pagePath) {
                continue;
            }
            loction = routeIndex;
            routes.splice(loction, 1);
        }

        if (routes.length > 10) {
            routes.shift();
        }

        routes.push(_obj);
        window.localStorage.setItem('vcPageRoute', JSON.stringify(routes));
    }

    vcFramework.deletePageRouteToLocal = function() {
        let routes = vcFramework.getPageRouteFromLocal();
        routes.pop();
        window.localStorage.setItem('vcPageRoute', JSON.stringify(routes));
    }

    vcFramework.recoverComponentByPageRoute = function() {
        let _hash = location.hash;

        let routes = vcFramework.getPageRouteFromLocal();

        if (routes.length < 1) {
            return;
        }
        let _tmpRoute = null;
        let loction = 0;
        for (let routeIndex = 0; routeIndex < routes.length; routeIndex++) {
            _tmpRoute = routes[routeIndex];
            if (!_tmpRoute.pagePath || _tmpRoute.pagePath != _hash) {
                continue;
            }

            for (key in _tmpRoute.pageData) {
                vcFramework.component[key] = _tmpRoute.pageData[key];
            }
            loction = routeIndex;
            routes.splice(loction, 1);
        }

        window.localStorage.setItem('vcPageRoute', JSON.stringify(routes));
    }

    /**
     * pageData = {
     *      pagePath:'',
     *      pageData:{
     * 
     * }
     * }
     */
    vcFramework.saveComponentToPageRoute = function() {
        let _component = vcFramework.component;
        let _hash = location.hash;

        if (!_hash) {
            return;
        }

        let _pageData = {
            pagePath: _hash,
            pageData: {}
        }

        for (_key in _component) {
            if (_key.startsWith('$') || _key.startsWith('_') || typeof _key == 'function') {
                continue;
            }
            _pageData.pageData[_key] = _component[_key];
        }
        vcFramework.setPageRouteToLocal(_pageData);
    }

    document.addEventListener('initVcFrameworkFinish', function(e) {
        //寻找当前页面是否在路由中 如果有恢复下数据，并做弹出
        vcFramework.recoverComponentByPageRoute();
    }, false);


    vcFramework.getTabFromLocal = function() {
        let tabStr = window.sessionStorage.getItem('vcTab');

        let tabs = [];

        if (!tabStr) {
            window.sessionStorage.setItem('vcTab', JSON.stringify(tabs))
        } else {
            tabs = JSON.parse(tabStr);
        }

        return tabs;
    }

    vcFramework.setTabToLocal = function(_obj) {
        let tabs = vcFramework.getTabFromLocal();

        //判断是否已经有 如果有则删除
        for (let tabIndex = 0; tabIndex < tabs.length; tabIndex++) {
            _tmpTab = tabs[tabIndex];
            if (_tmpTab.url == _obj.url) {
                return;
            }
        }
        if (tabs.length > 10) {
            tabs.shift();
        }
        tabs.push(_obj);
        window.sessionStorage.setItem('vcTab', JSON.stringify(tabs));
    }

    vcFramework.deleteTabToLocal = function(_obj) {
        let tabs = vcFramework.getTabFromLocal();
        for (let tabIndex = 0; tabIndex < tabs.length; tabIndex++) {
            _tmpTab = tabs[tabIndex];
            console.log(_tmpTab[tabIndex], _obj)
            if (_tmpTab.url == _obj.url) {
                tabs.splice(tabIndex, 1);
            }
        }
        window.sessionStorage.setItem('vcTab', JSON.stringify(tabs));
    }

    vcFramework.clearTabToLocal = function() {
        let tabs = [];
        window.sessionStorage.setItem('vcTab', JSON.stringify(tabs));
    }
})(window.vcFramework);

/**
 * 身份证号码信息提取
 * 18位身份证
 * 1生日2性别3年龄
 */
(function(vcFramework) {
    vcFramework.idCardInfoExt = function(idCard, type) {
        //通过身份证号计算年龄、性别、出生日期
        if (type == 1) {
            //获取出生日期
            birth = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
            return birth;
        }
        if (type == 2) {
            //获取性别
            if (parseInt(idCard.substr(16, 1)) % 2 == 1) {
                //男
                return 0;
            } else {
                //女
                return 1;
            }
        }
        if (type == 3) {
            //获取年龄
            var myDate = new Date();
            var month = myDate.getMonth() + 1;
            var day = myDate.getDate();
            var age = myDate.getFullYear() - idCard.substring(6, 10) - 1;
            if (idCard.substring(10, 12) < month || idCard.substring(10, 12) == month && idCard.substring(12, 14) <= day) {
                age++;
            }
            return age;
        }
    }
})(window.vcFramework);