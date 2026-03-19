package org.lfan142.chapter1;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

public class SynchronizedFactorizer  implements Servlet {

    private BigInteger lastNumber;
    private BigInteger[] lastFactor;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override

    public synchronized void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if((i == null && lastNumber==null) || (i!=null && i.equals(lastNumber))){
            encodedIntoResponse(servletResponse, lastFactor);
        } else{
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactor = factors;
            encodedIntoResponse(servletResponse, lastFactor);
        }
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodedIntoResponse(ServletResponse servletResponse, BigInteger[] lastFactor) {

    }

    private BigInteger extractFromRequest(ServletRequest servletRequest) {
        return null;
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {

    }
}
