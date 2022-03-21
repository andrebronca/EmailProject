package br.com.emailproject.dto;

import br.com.emailproject.model.Email;

public class EmailLayout {
	private final String QL = "<br />"; //Quebra de Linha

	public Email montarEmailAdministrador(String destinatario, String assunto) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("A/C Administrador"+ QL + QL)
			.append("Solicitando alteração da senha do sistema!"+ QL + QL);
		
		gerarAssinatura(sb);
		gerarRodape(sb);
		
		return new Email(destinatario, assunto, sb.toString());
	}
	
	private String gerarAssinatura(StringBuilder sb) {
		return sb
				.append("Att.:"+ QL)
				.append("Operador de Caixa"+ QL + QL).toString();
	}

	private String gerarRodape(StringBuilder txt) {
		return txt.append("E-mail automático. Favor não responder esse e-mail.").toString();
	}
}
