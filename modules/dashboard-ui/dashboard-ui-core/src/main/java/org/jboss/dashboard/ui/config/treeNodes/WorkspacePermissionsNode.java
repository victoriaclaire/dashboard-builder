/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.dashboard.ui.config.treeNodes;

import org.jboss.dashboard.ui.config.AbstractNode;
import org.jboss.dashboard.ui.config.components.permissions.PermissionsPropertiesHandler;
import org.jboss.dashboard.users.UserStatus;
import org.jboss.dashboard.workspace.Workspace;
import org.jboss.dashboard.security.WorkspacePermission;
import org.jboss.dashboard.workspace.Workspace;

public class WorkspacePermissionsNode extends AbstractNode {
    private static transient org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WorkspacePermissionsNode.class.getName());

    private PermissionsPropertiesHandler permissionsPropertiesHandler;

    public PermissionsPropertiesHandler getPermissionsPropertiesHandler() {
        return permissionsPropertiesHandler;
    }

    public void setPermissionsPropertiesHandler(PermissionsPropertiesHandler permissionsPropertiesHandler) {
        this.permissionsPropertiesHandler = permissionsPropertiesHandler;
    }

    public boolean isEditable() {
        WorkspaceNode parent = (WorkspaceNode) getParent();
        Workspace workspace;
        try {
            workspace = parent.getWorkspace();
            WorkspacePermission editPerm = WorkspacePermission.newInstance(workspace, WorkspacePermission.ACTION_EDIT_PERMISSIONS);
            return super.isEditable() && UserStatus.lookup().hasPermission(editPerm);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return false;
    }

    public boolean isExpandible() {
        return super.isExpandible() && isEditable();
    }

    public String getId() {
        return "permissions";
    }

    public boolean onEdit() {
        try {
            getPermissionsPropertiesHandler().reset();
            getPermissionsPropertiesHandler().setPermissionClass(WorkspacePermission.class);
            getPermissionsPropertiesHandler().setResourceName(WorkspacePermission.getResourceName(((WorkspaceNode) getParent()).getWorkspace()));
            getPermissionsPropertiesHandler().setWorkspaceId(((WorkspaceNode) getParent()).getWorkspaceId());
        } catch (Exception e) {
            log.error("Error: ", e);
            return false;
        }
        return true;
    }

}
