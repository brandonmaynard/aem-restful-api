/*
 * Overridden initwcm javascript overlay.
 * OOTB initwcm.js file is found in /libs/wcm/foundation/components/page/initwcm.js
 */

"use strict";

var global = this;

use(["../utils/AuthoringUtils.js"], function (AuthoringUtils) {

    var _getUndoConfig = function () {
        var undoCfg = undefined;
        if (global.Packages && global.sling) {
            var undoConfigService = global.sling.getService(global.Packages.com.day.cq.wcm.undo.UndoConfigService);
            var javaStringWriter = new java.io.StringWriter();
            undoConfigService.writeClientConfig(javaStringWriter);
            undoCfg = javaStringWriter.toString();
        }
        return undoCfg;
    };

    var _getDialogPath = function () {
        var dialogPath = undefined;
        if (global.editContext) {
            dialogPath = global.editContext.getComponent().getDialogPath();
        }
        return dialogPath;
    };

    return {
        isTouchAuthoring: AuthoringUtils.isTouch,
        dialogPath: _getDialogPath(),
        undoConfig: _getUndoConfig(),
        wcmmode: global.wcmmode
    };
});
