/*
 * Implementa��o do Bloqueto de Cobran�as do Banco do Brasil
 * - Conv�nio com 4 posi��es 
 * 
 * scoelho@stefanini.com
 */
package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public class BloquetoBBConvenio4 extends BloquetoBBImpl implements BloquetoBB {

	@Override
	protected void validaDados() throws ManagerException {

		if (codigoBanco == null || codigoBanco.length() != 3) {
			throw new ManagerException(
					"C�digo do Banco n�o informado ou com tamanho diferente de 3 posi��es");
		}

		if (codigoMoeda == null || codigoMoeda.length() != 1) {
			throw new ManagerException(
					"C�digo de moeda n�o informado ou inv�lido");
		}

		if (dataVencimento == null) {
			throw new ManagerException("Data de vencimento n�o informada");
		}

		if (valor == null) {
			throw new ManagerException(
					"Valor do bloqueto bancário n�o informado");
		}

		if (numeroConvenioBanco == null || numeroConvenioBanco.length() != 4) {
			throw new ManagerException(
					"n�mero de conv�nio n�o informado ou o conv�nio informado � inv�lido. O conv�nio deve ter 4 posi��es");
		}

		if (complementoNumeroConvenioBancoSemDV == null
				&& complementoNumeroConvenioBancoSemDV.length() != 7) {
			throw new ManagerException(
					"Complemento do n�mero do conv�nio n�o informado. O complemento deve ter 7 posi��es");
		}

		if (numeroAgenciaRelacionamento == null
				|| numeroAgenciaRelacionamento.length() != 4) {
			throw new ManagerException(
					"n�mero da ag�ncia de Relacionamento n�o informado. O n�mero da ag�ncia deve ter 4 posi��es");
		}

		if (contaCorrenteRelacionamentoSemDV == null
				|| contaCorrenteRelacionamentoSemDV.length() != 8) {
			throw new ManagerException(
					"Conta corrente de relacionamento n�o informada. O n�mero da conta deve ter 8 posi��es");
		}

		if (tipoCarteira == null || tipoCarteira.length() != 2) {
			throw new ManagerException(
					"Tipo carteira n�o informado ou o valor � inv�lido");
		}

		if (dataBase == null) {
			throw new ManagerException("A database n�o foi informada.");
		}

	}

	public BloquetoBBConvenio4(String codigoBanco, String codigoMoeda,
			Date dataVencimento, Date dataBase, BigDecimal valor,
			String numeroConvenioBanco,
			String complementoNumeroConvenioBancoSemDV,
			String numeroAgenciaRelacionamento,
			String contaCorrenteRelacionamentoSemDV, String tipoCarteira)
			throws ManagerException {

		this.codigoBanco = codigoBanco;
		this.codigoMoeda = codigoMoeda;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.numeroConvenioBanco = numeroConvenioBanco;
		this.complementoNumeroConvenioBancoSemDV = complementoNumeroConvenioBancoSemDV;
		this.numeroAgenciaRelacionamento = numeroAgenciaRelacionamento;
		this.contaCorrenteRelacionamentoSemDV = contaCorrenteRelacionamentoSemDV;
		this.tipoCarteira = tipoCarteira;
		this.dataBase = dataBase;

		validaDados();

	}

	@Override
	protected String getLDNumeroConvenio() {
		
		String convenio = String.format("%04d", Long.valueOf(numeroConvenioBanco));
	return String.format("%s.%s",  convenio.substring(0,1), convenio.substring(1,5));

	}

	// TODO: @sandro - refatorar os m�todos getCodigoBarrasSemDigito() e
	// getCodigoBarras()

	@Override
	protected String getCodigoBarrasSemDigito() {

		init();

		StringBuilder buffer = new StringBuilder();
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		buffer.append(numeroConvenioBanco);
		buffer.append(complementoNumeroConvenioBancoSemDV);
		buffer.append(numeroAgenciaRelacionamento);
		buffer.append(contaCorrenteRelacionamentoSemDV);
		buffer.append(tipoCarteira);

		return buffer.toString();
	}

	@Override
	public String getCodigoBarras() {

		init();

		StringBuilder buffer = new StringBuilder();
		
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda); 
		buffer.append(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito())); 
		
		buffer.append(fatorVencimento); 
		buffer.append(getValorFormatado()); 
		buffer.append(numeroConvenioBanco); 
		
		buffer.append(complementoNumeroConvenioBancoSemDV);
		buffer.append(numeroAgenciaRelacionamento);
		buffer.append(contaCorrenteRelacionamentoSemDV); 
		buffer.append(tipoCarteira); 

		return buffer.toString();
	}

}
