package br.com.emailproject.util;

import org.apache.log4j.Logger;

public class LogUtil {
	
	
	private LogUtil() {}
	
	public static Logger getLogger(Object object) {
		return Logger.getLogger(object.getClass());
		/*
		 * Essa versão do log4j 1.2 tem bug, vulnerabilidade.
		 * Deve ser trocada pela 2.17.1
		 */
	}
}
