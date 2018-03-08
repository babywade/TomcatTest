package com.runoob.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloForm
 * URL输入中文表单数据有错，英文显示正确
 */
@WebServlet("/HelloForm")
public class HelloForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloForm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		String title = "Using POST method to read FORM data";
		
		String name = new String(request.getParameter("name").getBytes("ISO8859-1"), "UTF-8");
		String docType = "<!DOCTYPE html> \n";
		out.println(docType +
				"<html>\n" +
				"<head><title>" + title + "</title></head>\n" + 
				"<body bgcolor=\"f0f0f0\">\n" + 
				"<h1 align=\"center\">" + title + "</h1>\n" + 
				"<ul>\n" + 
				"  <li><b>站点名</b>:" + name + "\n" +
				"  <li><b>网址</b>:" + request.getParameter("url") + "\n" +
				"</ul>\n" +
				"</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
