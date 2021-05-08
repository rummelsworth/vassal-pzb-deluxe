package rummelsworth.vassal.pzbdeluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JTextField;

import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public class AppendSelectionForChat extends AbstractConfigurable {
    private JButton button_appendSelectionForChat;

    @Override
    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }

    @Override
    public HelpFile getHelpFile() {
        return null;
    }

    @Override
    public void removeFrom(Buildable arg0) {
        Map map = (Map) arg0;
        map.getToolBar().remove(button_appendSelectionForChat);
    }

    @Override
    public void addTo(Buildable arg0) {
        button_appendSelectionForChat = new JButton("Append Selection For Chat");
        button_appendSelectionForChat.setToolTipText("Append a text summary of all selected pieces to the chat input.");
        button_appendSelectionForChat.setAlignmentY(0);
        button_appendSelectionForChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                appendPieceSelectionToChatInput();
            }
        });

        Map map = (Map) arg0;
        map.getToolBar().add(button_appendSelectionForChat);
    }

    @Override
    public String[] getAttributeDescriptions() {
        return new String[0];
    }

    @Override
    public Class<?>[] getAttributeTypes() {
        return new Class<?>[0];
    }

    @Override
    public String[] getAttributeNames() {
        return new String[0];
    }

    @Override
    public String getAttributeValueString(String arg0) {
        return null;
    }

    @Override
    public void setAttribute(String arg0, Object arg1) {
    }

    private void appendPieceSelectionToChatInput() {
        SortedMap<String, List<String>> unitsPerHex = new TreeMap<String, List<String>>();
        GamePiece[] pieces = Map.activeMap.getPieces();
        for (int piecesIndex = 0; piecesIndex < pieces.length; ++piecesIndex) {
            GamePiece piece = pieces[piecesIndex];
            if (piece instanceof Stack) {
                Stack stack = (Stack) piece;
                int stackSize = stack.getPieceCount();
                if (stackSize > 0) {
                    for (int stackIndex = 0; stackIndex < stackSize; ++stackIndex) {
                        GamePiece unit = stack.getPieceAt(stackIndex);
                        String currentBoard = (String) unit.getProperty("CurrentBoard");
                        final String boardPrefix = "PZB Board ";
                        if (currentBoard != null && currentBoard.startsWith(boardPrefix)) {
                            Boolean unitIsSelected = (Boolean) unit.getProperty("Selected");
                            if (unitIsSelected) {
                                String boardId = currentBoard.substring(boardPrefix.length());

                                String locationName = (String) unit.getProperty("LocationName");
                                int indexOfRowId = 0;
                                while (!Character.isDigit(locationName.charAt(indexOfRowId))) {
                                    ++indexOfRowId;
                                }
                                String columnId = locationName.substring(0, indexOfRowId);
                                String rowId = locationName.substring(indexOfRowId);
                                String hexName = String.join("-", boardId, columnId, rowId);

                                List<String> units = unitsPerHex.get(hexName);
                                if (units == null) {
                                    units = new ArrayList<String>();
                                    unitsPerHex.put(hexName, units);
                                }
                                String unitName = (String) unit.getProperty("BasicName");
                                units.add(unitName);
                            }
                        }
                    }
                }
            }
        }

        List<String> hexTexts = new ArrayList<String>();
        for (Entry<String, List<String>> hex : unitsPerHex.entrySet()) {
            String unitsText = String.join(", ", hex.getValue());
            String hexText = "[" + unitsText + "](" + hex.getKey() + ")";
            hexTexts.add(hexText);
        }

        String selectionText = String.join(" + ", hexTexts);

        JTextField chatInputField = GameModule.getGameModule().getChatter().getInputField();
        String currentChatInput = chatInputField.getText();
        chatInputField.setText(currentChatInput + selectionText);
    }
}
