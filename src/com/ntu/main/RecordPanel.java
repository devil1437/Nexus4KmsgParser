
package com.ntu.main;

import java.util.logging.Logger;

import javax.swing.JPanel;

public class RecordPanel extends JPanel {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7365266521871070084L;

    private static final Logger LOG = Logger.getLogger("RecordPanel");

    public static final int SMALL_RESULT = 0;
    public static final int BIG_RESULT = 0;
    public static final int SECONDTIER_RESULT = 0;

    public int mPosition[];
    public int mId;
    public int mType;
    public String mData;
    private RecordFrame mFrame;

    // public RecordPanel(int x, int y, int id, int type){
    // mId = id;
    // mPosition = new int[2];
    // mPosition[0] = x;
    // mPosition[1] = y;
    // mType = type;
    // mFrame = new RecordFrame();
    //
    // // Create border.
    // if(x == 0){
    // this.setBorder(BorderFactory.createMatteBorder(0, MainFrame.BORDER_SIZE,
    // 0, 0, Color.BLACK));
    // }
    // else if(x % MainFrame.MODEL_LENGTH_IN_X == MainFrame.BIG_RESULT_IN_X){
    // this.setBorder(BorderFactory.createMatteBorder(0, MainFrame.BORDER_SIZE,
    // 0, 0, Color.BLACK));
    // }
    // else if(x % MainFrame.MODEL_LENGTH_IN_X == MainFrame.SECOND_TIER_IN_X){
    // this.setBorder(BorderFactory.createMatteBorder(0, 0, 0,
    // MainFrame.BORDER_SIZE, Color.BLACK));
    // }
    // }
    //
    // public void setImage(ImageIcon image){
    // if(this.getComponentCount() > 0){
    // this.removeAll();
    // }
    // JLabel temp = new JLabel(image);
    // temp.addMouseListener(new MouseAdapter(){
    // public void mouseClicked(MouseEvent e) {
    // // TODO Auto-generated method stub
    // mFrame.setVisible(true);
    // }
    // });
    // this.add(temp);
    // }
    //
    // public void setData(Record record, Classifier classifier){
    // if(mType != SMALL_RESULT){
    // LOG.info("Error: setDate() get wrong type.");
    // return;
    // }
    // mData = record.toString();
    // mData += classifier.toString();
    // mFrame.setText(mData);
    // }
}
