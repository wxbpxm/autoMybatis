package fuku.automybatis.pageconfig.ui;
 
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author wangxuebiao
 */
public class MyConfigTable extends JTable {
    private static final long serialVersionUID = 1L;  
    
    private int[] booleanColIndx;
    
    private int[] typeColIndx;
    
    public MyConfigTable(int[] booleanColIndx, int[] typeColIndx) {
        this.booleanColIndx = booleanColIndx;
        this.typeColIndx = typeColIndx;
    }
            
    @Override  
    public TableCellEditor getCellEditor(int row, int column) {
        if (isBoolean(column)) {
        	String[] values = new String[] { "1", "0"};  
           return new DefaultCellEditor(new JComboBox(values));  
        }
        if (isType(column)) {
        	String[] values = new String[] { "文本", "数值", "下拉框", "图片", "日期", "时间"};  
        	return new DefaultCellEditor(new JComboBox(values));  
        }
       
        return super.getCellEditor(row, column);  
    } 
    
    private Boolean isBoolean(int indx) {
        if(booleanColIndx == null) {
            return false;
        }
        
        for(int s : booleanColIndx) {
            if (s == indx) {
                return true;
            }
        }
        
        return false;
    }
    
    private Boolean isType(int indx) {
    	if(typeColIndx == null) {
    		return false;
    	}
    	
    	for(int s : typeColIndx) {
    		if (s == indx) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public void setBooleanColIndx(int[] booleanColIndx) {
        this.booleanColIndx = booleanColIndx;
    }
     
}
