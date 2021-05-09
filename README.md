# "Append Selection For Chat", an extension for PanzerBlitz Deluxe on Vassal

This extension provides a button that will append a text summary of the current unit selection into Vassal's chat input field. The summary includes unit names (e.g. "20mm AA" etc.) and board locations (e.g. "2-D-4" for hex D4 on board 2). Handy for PBEM when you're declaring your attacks in full detail before resolving them.

This is the Vassal module page for PanzerBlitz:

http://www.vassalengine.org/wiki/Module:PanzerBlitz

This is the actual download page for the "deluxe" module:

https://dddgamedesigns.com/vassal-products/

The latter page is not (as of this writing) linked in the module page's table of downloads. It's a bit buried down in the paragraphs below the table.

This repo contains the source code for a custom class extension (vmdx). The vmdx subfolder, when zipped, is the extension file itself. If you update the source code, you have to compile it and manually update the class files in the vmdx. If the extension is updated in the Vassal editor, you should decompress the modified extension and copy-replace all those files into the vmdx subfolder.
