package pers.xyy.api_replacement_tool.service.impl;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.xyy.api_replacement_tool.ApiReplacementToolApplication;
import pers.xyy.api_replacement_tool.ResponseCode;
import pers.xyy.api_replacement_tool.exceptions.ImportsIncompleteException;
import pers.xyy.api_replacement_tool.model.Line;
import pers.xyy.api_replacement_tool.model.ReplacedCode;
import pers.xyy.api_replacement_tool.resource.Response;
import pers.xyy.api_replacement_tool.service.IReplaceService;
import pers.xyy.api_replacement_tool.service.IReplacedCodeService;
import pers.xyy.api_replacement_tool.service.IVisitorService;
import pers.xyy.api_replacement_tool.utils.BoundedStack;
import pers.xyy.api_replacement_tool.utils.FileUtil;

import java.util.List;

@Service
public class ReplaceService implements IReplaceService {

    @Autowired
    private BoundedStack<CompilationUnit> stack;

    private ReplacedCode replacedCode;

    @Autowired
    private IReplacedCodeService service;

    @Autowired
    private IVisitorService visitorService;

    @Override
    public void clear() {
        stack.clear();
        replacedCode = null;
    }

    @Override
    public String getExamples() {
        try {
            String path = ApiReplacementToolApplication.class.getResource("/examples/ExampleA").toURI().getPath();
            String result = FileUtil.readFile(path);
            //System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response analyze(String code) {
        Response response = analyzeCode(code);
        stack.push(replacedCode.getCu().clone());
        return response;
    }

    @Override
    public Response replace(int index) {
        try {
            visitorService.replace(replacedCode, index);
            service.initLines(replacedCode);
            return new Response(replacedCode.getLines());
        } catch (ParseProblemException ppe) {
            return new Response(ResponseCode.CODE_SYNTAX.getValue(), ppe.getMessage());
        } catch (ImportsIncompleteException iie) {
            return new Response(ResponseCode.IMPORTS_INCOMPLETE.getValue(), iie.getMessage());
        } catch (Exception e) {
            return new Response(ResponseCode.UNKNOWN_EXCEPTION.getValue(), e.getMessage());
        }
    }

    @Override
    public Response revert() {
        String code = stack.pop().toString();
        Response response = analyzeCode(code);
        if (stack.isEmpty())
            response.setResponseCode(ResponseCode.SUCCESS_CANNOT_REVERT.getValue());
        return response;
    }

    @Override
    public Response replaceAll() {
        try {
            visitorService.replaceAll(replacedCode);
            service.initLines(replacedCode);
            return new Response(replacedCode.getLines());
        } catch (ParseProblemException ppe) {
            replacedCode = service.getReplaceCode(stack.peek().toString());
            return new Response(ResponseCode.CODE_SYNTAX.getValue(), ppe.getMessage());
        } catch (ImportsIncompleteException iie) {
            replacedCode = service.getReplaceCode(stack.peek().toString());
            return new Response(ResponseCode.IMPORTS_INCOMPLETE.getValue(), iie.getMessage());
        } catch (Exception e) {
            replacedCode = service.getReplaceCode(stack.peek().toString());
            return new Response(ResponseCode.UNKNOWN_EXCEPTION.getValue(), e.getMessage());
        }

    }

    private Response analyzeCode(String code) {
        try {
            replacedCode = service.getReplaceCode(code);
            List<Line> lines = replacedCode.getLines();
            for (Line line : lines) {
                System.out.println(line.getContent());
                System.out.println(line.getIndex());
            }
            return new Response(replacedCode.getLines());
        } catch (ParseProblemException ppe) {
            return new Response(ResponseCode.CODE_SYNTAX.getValue(), ppe.getMessage());
        } catch (ImportsIncompleteException iie) {
            return new Response(ResponseCode.IMPORTS_INCOMPLETE.getValue(), iie.getMessage());
        }
    }


//    public static void main(String[] args) {
//        IReplaceService service = new ReplaceService();
//        List<Line> lines = service.analyze("import javax.swing.*;\nimport javax.swing.text.BoxView;\nimport javax.swing.text.View;\n\npublic class ExampleA {\n\n    public void testMethod(String str, JComponent jp){\n        System.out.println(str);\n        Date date = new Date();\n        int month = 5;\n        date.setMonth(5);\n        long time = date.UTC(2017,3,3,3,3,3);\n        date.getYear();\n        date.getTimezoneOffset();\n        System.out.println(getMonth(\"\"));\n        JMenuBar menuBar = new JMenuBar();\n        menuBar.getComponentAtIndex(5);\n        View view = new BoxView(null,5);\n        view.viewToModel(1,1,null);\n        JTable jTable = new JTable();\n        JScrollPane jScrollPane = JTable.createScrollPaneForTable(jTable);\n    }\n    public int getMonth(String str){\n        return new Date().getMonth();\n    }\n\n\n}\n");
//        for(Line line : lines){
//            System.out.println(line.getContent());
//        }
//    }
}
