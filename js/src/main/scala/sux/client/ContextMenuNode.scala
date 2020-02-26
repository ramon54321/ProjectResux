package sux.client

import sux.client.rendering.FrameInfo

class ContextMenuNode(val name: String, val action: FrameInfo => Unit, val children: ContextMenuNode*) {

}
