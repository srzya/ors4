package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * Marksheet functionality Controller. Performs operation for add, update,
 * delete and get Marksheet
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@ WebServlet(name="MarksheetCtl",urlPatterns={"/ctl/MarksheetCtl"})
public class MarksheetCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(MarksheetCtl.class);
    /**
     * Loads list and other data required to display at HTML form
     * 
     * @param request
     */

    @Override
    protected void preload(HttpServletRequest request) {
        StudentModel model = new StudentModel();
        try {
            List l = model.list();
            request.setAttribute("studentList", l);
           
            System.out.println("list size in marksheet ctl-------->"+l.size());
        } catch (ApplicationException e) {
            log.error(e);
        }

    }
    /**
     * Validates input data entered by User
     * 
     * @param request
     * @return
     */
  
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("MarksheetCtl Method validate Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo",
                    PropertyReader.getValue("error.require", "Roll Number"));
            pass = false;
        }
        else if(DataValidator.isRollNO(request.getParameter("rollNo"))){
			request.setAttribute("rollNo", PropertyReader.getValue("error.rollno","Roll No"));
			pass=false;
		}

        if (DataValidator.isNull(request.getParameter("studentId"))) {
            request.setAttribute("studentId",
                    PropertyReader.getValue("error.require", "Student Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("physics"))) {
            request.setAttribute("physics",PropertyReader.getValue("error.require", "Physics Marks"));
            pass = false;
        }
       
        else if (!DataValidator.isMarks(request.getParameter("physics"))) {
        	
            request.setAttribute("physics",PropertyReader.getValue("error.marks","Physics Marks"));
            pass = false;
          	
                  
        }
        
        
        if (DataValidator.isNull(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry",PropertyReader.getValue("error.require", "Chemistry Marks"));
            pass = false;
        }
        else if (!DataValidator.isMarks(request.getParameter("chemistry"))) {
        	
            request.setAttribute("chemistry",PropertyReader.getValue("error.marks","Chemistry Marks"));
            pass = false;
          
                  
        }
           
        if (DataValidator.isNull(request.getParameter("maths"))) {
            request.setAttribute("maths",
                    PropertyReader.getValue("error.require", "Maths Marks"));
            pass = false;
        }
        
            else if (!DataValidator.isMarks(request.getParameter("maths"))) {
        	
            request.setAttribute("maths",PropertyReader.getValue("error.marks","Maths Marks"));
            pass = false;
          
                  
        }
            

        log.debug("MarksheetCtl Method validate Ended");

        return pass;
    }
    /**
     * Populates bean object from request parameters
     * 
     * @param request
     * @return
     */

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("MarksheetCtl Method populatebean Started");

        MarksheetBean bean = new MarksheetBean();

        bean.setId(DataUtility.getLong((request.getParameter("id"))));

        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

        bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));

        bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));

        bean.setMaths(DataUtility.getInt(request.getParameter("maths")));

        bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));

        populateDTO(bean, request);

        System.out.println("Population done");

        log.debug("MarksheetCtl Method populatebean Ended");

        return bean;
    }

    /**
     * Contains Display logics
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("MarksheetCtl Method doGet Started");
System.out.println("marksheet ctl");


//student k login k bad marksheetctl nai khulega...
/*UserBean b=(UserBean)request.getSession(true).getAttribute("user");
if(b.getRoleId()!=1){
	ServletUtility.redirect(ORSView.ERROR_CTL, request, response);
return;
}*/
      //  String op = DataUtility.getString(request.getParameter("operation"));
        MarksheetModel model = new MarksheetModel();
        long id =	 DataUtility.getLong(request.getParameter("id"));
        if (id > 0) {
            MarksheetBean bean;
            try {
                bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("MarksheetCtl Method doGet Ended");
    }

    /**
     * Contains Submit logics
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        log.debug("MarksheetCtl Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        // get model
        MarksheetModel model = new MarksheetModel();

        long id = DataUtility.getLong(request.getParameter("id"));
        System.out.println("id-----------"+id);
        if (OP_SAVE.equalsIgnoreCase(op)||OP_UPDATE.equalsIgnoreCase(op)) {

            MarksheetBean bean = (MarksheetBean) populateBean(request);
            try {
                if (id > 0) {
                    model.update(bean);
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Data is successfully Updated",request);

                } else {
                    long pk = model.add(bean);
                   // ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Data is successfully saved",request);

                    /*bean.setId(pk);*/
                }
                
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Roll no already exists",
                        request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            MarksheetBean bean = (MarksheetBean) populateBean(request);
            System.out.println("in try");
            try {
                model.delete(bean);
                ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request,
                        response);
                System.out.println("in try");
                return;
            } catch (ApplicationException e) {
                System.out.println("in catch");
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request,
                    response);
            return;

        }
        ServletUtility.forward(getView(), request, response);

        log.debug("MarksheetCtl Method doPost Ended");
    }

    /**
     * Returns the VIEW page of this Controller
     * 
     * @return
     */

    @Override
    protected String getView() {
        return ORSView.MARKSHEET_VIEW;
    }
}
