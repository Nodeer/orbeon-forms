/**
 *  Copyright (C) 2008 Orbeon, Inc.
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU Lesser General Public License as published by the Free Software Foundation; either version
 *  2.1 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
 */
package org.orbeon.oxf.xforms.function.xxforms;

import org.orbeon.oxf.xforms.InstanceData;
import org.orbeon.oxf.xforms.function.XFormsFunction;
import org.orbeon.saxon.expr.Expression;
import org.orbeon.saxon.expr.XPathContext;
import org.orbeon.saxon.om.Item;
import org.orbeon.saxon.om.NodeInfo;
import org.orbeon.saxon.trans.XPathException;
import org.orbeon.saxon.value.QNameValue;

public class XXFormsType extends XFormsFunction {

    public Item evaluateItem(XPathContext xpathContext) throws XPathException {

        final Expression nodesetExpression = argument[0];
        // "If the argument is omitted, it defaults to a node-set with the context node as its only
        // member."
        final Item item;
        if (nodesetExpression == null)
            item = xpathContext.getContextItem();
        else
            item = nodesetExpression.iterate(xpathContext).next();

        // Return () if we can't access the node
        if (item == null || !(item instanceof NodeInfo))
            return null;

        // Get type from node
        final String typeExplodedQName = InstanceData.getType((NodeInfo) item);
        if (typeExplodedQName == null)
            return null;

        // Create Saxon QName
        final String uri;
        final String localname;
        {
            int openIndex = typeExplodedQName.indexOf("{");
            if (openIndex == -1) {
                uri = "";
                localname = typeExplodedQName;
            } else {
                uri = typeExplodedQName.substring(openIndex + 1, typeExplodedQName.indexOf("}"));
                localname = typeExplodedQName.substring(typeExplodedQName.indexOf("}") + 1);
            }
        }

        return new QNameValue(null, uri, localname, null);
    }
}
