package pers.xyy.api_replacement_tool.examples;

import javax.swing.*;
import javax.swing.text.BoxView;
import javax.swing.text.View;
import java.util.Date;
import javax.swing.JScrollPane;

public class C {

    public void testMethod(String str, JComponent jp) {
        System.out.println(str);
        Date date = new Date();
        int month = 5;
        date.setMonth(5);
        long time = date.UTC(2017, 3, 3, 3, 3, 3);
        int year = date.getYear();
        int timezoneoffset = date.getTimezoneOffset();
        System.out.println(getMonth(""));
        JMenuBar menuBar = new JMenuBar();
        menuBar.getComponentAtIndex(5);
        View view = new BoxView(null, 5);
        view.viewToModel(1, 1, null);
        JTable jTable = new JTable();
        JScrollPane jScrollPane = JTable.JScrollPane(jTable);
    }

    public int getMonth(String str) {
        return new Date().getMonth();
    }
}
