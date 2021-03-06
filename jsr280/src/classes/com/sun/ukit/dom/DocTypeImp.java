/*
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER 
 *  
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License version 
 * 2 only, as published by the Free Software Foundation. 
 *  
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License version 2 for more details (a copy is 
 * included at /legal/license.txt). 
 *  
 * You should have received a copy of the GNU General Public License 
 * version 2 along with this work; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 
 * 02110-1301 USA 
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa 
 * Clara, CA 95054 or visit www.sun.com if you need additional 
 * information or have any questions.
 */

package com.sun.ukit.dom;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.DOMException;

/**
 * DOM document type node implementation.
 *
 * @see org.w3c.dom.Node
 */

/* pkg */ final class DocTypeImp
	extends XNode
	implements DocumentType
{
	/** Document public identifier. */
	private String pubid;
	/** Document system identifier. */
	private String sysid;

	/** Document entities. */
	/* pkg */ NodeMapImp entmap;
	/** Document notations. */
	/* pkg */ NodeMapImp notmap;

	/**
	 * Constructs document type object from other document type.
	 */
	 /* pkg */ DocTypeImp(DocTypeImp doctyp, boolean deep)
	 {
	 	super(doctyp, deep);

	 	pubid = doctyp.getPublicId();
	 	sysid = doctyp.getSystemId();

		entmap = new NodeMapImp();
		notmap = new NodeMapImp();

	 	//		Copy entities
		for (int idx = 0; idx < doctyp.entmap.getLength(); idx++) {
			entmap._append((XNode)(doctyp.entmap.item(idx).cloneNode(true)));
		}
	 	//		Copy notations
		for (int idx = 0; idx < doctyp.notmap.getLength(); idx++) {
			notmap._append((XNode)(doctyp.notmap.item(idx).cloneNode(true)));
		}
	 }

	/**
	 * Constructs processing instruction object from its owner document.
	 */
	 /* pkg */ DocTypeImp(
	 		String qualifiedName, String publicId, String systemId)
	 {
	 	super(null, qualifiedName, null);

		if (qualifiedName != null)
			XNode._checkName(qualifiedName, true);

	 	pubid = publicId;
	 	sysid = systemId;

		entmap = new NodeMapImp();
		notmap = new NodeMapImp();
	 }

	/**
	 * A code representing the type of the underlying object, as defined above.
	 */
	public short getNodeType()
	{
		return DOCUMENT_TYPE_NODE;
	}

	/**
	 * Returns a duplicate of this node, i.e., serves as a generic copy 
	 * constructor for nodes. The duplicate node has no parent; (
	 * <code>parentNode</code> is <code>null</code>.).
	 * <br>Cloning an <code>Element</code> copies all attributes and their 
	 * values, including those generated by the XML processor to represent 
	 * defaulted attributes, but this method does not copy any text it 
	 * contains unless it is a deep clone, since the text is contained in a 
	 * child <code>Text</code> node. Cloning an <code>Attribute</code> 
	 * directly, as opposed to be cloned as part of an <code>Element</code> 
	 * cloning operation, returns a specified attribute (
	 * <code>specified</code> is <code>true</code>). Cloning any other type 
	 * of node simply returns a copy of this node.
	 * <br>Note that cloning an immutable subtree results in a mutable copy, 
	 * but the children of an <code>EntityReference</code> clone are readonly
	 * . In addition, clones of unspecified <code>Attr</code> nodes are 
	 * specified. And, cloning <code>Document</code>, 
	 * <code>DocumentType</code>, <code>Entity</code>, and 
	 * <code>Notation</code> nodes is implementation dependent.
	 *
	 * @param deep If <code>true</code>, recursively clone the subtree under 
	 *   the specified node; if <code>false</code>, clone only the node 
	 *   itself (and its attributes, if it is an <code>Element</code>). 
	 * @return The duplicate node.
	 */
	public Node cloneNode(boolean deep)
	{
		XNode node = new DocTypeImp(this, deep);
		_notifyUDH(UserDataHandler.NODE_CLONED, node);
		return node;
	}

	/**
	 * The name of DTD; i.e., the name immediately following the 
	 * <code>DOCTYPE</code> keyword.
	 */
	public String getName()
	{
		return getNodeName();
	}

	/**
	 * A <code>NamedNodeMap</code> containing the general entities, both 
	 * external and internal, declared in the DTD. Parameter entities are 
	 * not contained. Duplicates are discarded. For example in: 
	 * <pre>&lt;!DOCTYPE 
	 * ex SYSTEM "ex.dtd" [ &lt;!ENTITY foo "foo"&gt; &lt;!ENTITY bar 
	 * "bar"&gt; &lt;!ENTITY bar "bar2"&gt; &lt;!ENTITY % baz "baz"&gt; 
	 * ]&gt; &lt;ex/&gt;</pre>
	 *  the interface provides access to <code>foo</code> 
	 * and the first declaration of <code>bar</code> but not the second 
	 * declaration of <code>bar</code> or <code>baz</code>. Every node in 
	 * this map also implements the <code>Entity</code> interface.
	 * <br>The DOM Level 2 does not support editing entities, therefore 
	 * <code>entities</code> cannot be altered in any way.
	 */
	public NamedNodeMap getEntities()
	{
		return entmap;
	}

	/**
	 * A <code>NamedNodeMap</code> containing the notations declared in the 
	 * DTD. Duplicates are discarded. Every node in this map also implements 
	 * the <code>Notation</code> interface.
	 * <br>The DOM Level 2 does not support editing notations, therefore 
	 * <code>notations</code> cannot be altered in any way.
	 */
	public NamedNodeMap getNotations()
	{
		return notmap;
	}

	/**
	 * The public identifier of the external subset.
	 * @since DOM Level 2
	 */
	public String getPublicId()
	{
		return pubid;
	}

	/**
	 * The system identifier of the external subset.
	 *
	 * @since DOM Level 2
	 */
	public String getSystemId()
	{
		return sysid;
	}

	/**
	 * The internal subset as a string.The actual content returned depends on 
	 * how much information is available to the implementation. This may 
	 * vary depending on various parameters, including the XML processor 
	 * used to build the document.
	 *
	 * @since DOM Level 2
	 */
	public String getInternalSubset()
	{
		return null;
	}

	/**
	 * Return true if node or one of its parents is read only.
	 */
	protected boolean _isRO()
	{
		return true;
	}

	/**
	 * Sets node's owner document.
	 *
	 * @param ownerDoc New owner document.
	 */
	protected void _setDoc(XDoc ownerDoc)
	{
		super._setDoc(ownerDoc);

		for (int idx = 0; idx < entmap.getLength(); idx++) {
			((XNode)(entmap.item(idx)))._setDoc(ownerDoc);
		}
		for (int idx = 0; idx < notmap.getLength(); idx++) {
			((XNode)(notmap.item(idx)))._setDoc(ownerDoc);
		}
	}

	/**
	 * Returns string representation of the element.
	 */
	public String toString()
	{
		StringBuffer out = new StringBuffer();

		out.append("<!DOCTYPE ");
		out.append(getNodeName());
		if (getPublicId() != null) {
			out.append(" PUBLIC ");
			out.append(getPublicId());
			if (getSystemId() != null) {
				out.append(' ');
				out.append(getSystemId());
			}
		} else if (getSystemId() != null) {
			out.append(" SYSTEM ");
			out.append(getSystemId());
		}
		out.append(">");
	
		return out.toString();
	}
}
