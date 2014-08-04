package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public abstract class BloquetoBBImpl implements BloquetoBB {

	protected String codigoBanco;
	protected String codigoMoeda;
	protected String fatorVencimento;
	protected Date dataVencimento;
	protected Date dataBase;
	protected BigDecimal valor;
	protected String numeroConvenioBanco;
	protected String complementoNumeroConvenioBancoSemDV;
	protected String numeroAgenciaRelacionamento;
	protected String contaCorrenteRelacionamentoSemDV;
	protected String tipoCarteira;

	private int dvCodigoBarras;

	protected abstract void validaDados() throws ManagerException;

	/**
	 * Inicializa o fator de vencimento
	 */
	protected void setFatorVencimento() {

		long dias = diferencaEmDias(dataBase, dataVencimento);

		// TODO: EXPLICAR O QUE ESTE M�TODO EST� FAZENDO
		// Esse m�todo mostra os dias em que a fatura est� vencida

		fatorVencimento = String.format("%04d", dias);

	}

	/**
	 * Inicializa os valores, formata
	 */
	protected void init() {

		setFatorVencimento();

	}

	/**
	 * Retorna o valor formatado do boleto banc�rio
	 * 
	 * @return
	 */
	protected String getValorFormatado() {

		// TODO: Esse m�todo est� convertendo os numeros da linha digitavel em
		// decimais.
		return String.format(
				"%010d",
				Long.valueOf(valor.setScale(2, RoundingMode.HALF_UP).toString()
						.replace(".", "")));
	}

	/**
	 * Formata o n�mero do conv�nio da Linha Digit�vel
	 * 
	 * @return
	 */
	protected abstract String getLDNumeroConvenio();

	/**
	 * Retorna o c�digo de barras do Bloqueto
	 * 
	 * @return c�digo de barras
	 */
	protected abstract String getCodigoBarrasSemDigito();

	public abstract String getCodigoBarras();

	/**
	 * Campo 5 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo5() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		return buffer.toString();
	}

	/**
	 * Campo 4 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo4() {
		return String
				.valueOf(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
	}

	/**
	 * Campo 3 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo3() {
		return String.format("%s.%s", getCodigoBarras().substring(34, 39),
				getCodigoBarras().substring(39, 44));
	}

	/**
	 * Campo 2 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo2() {
		return String.format("%s.%s", getCodigoBarras().substring(24, 29),
				getCodigoBarras().substring(29, 34));
	}

	/**
	 * Calcula o digito verificador do campo
	 * 
	 * @param campo
	 * @return
	 */
	protected int digitoVerificadorPorCampo(String campo, boolean valor) {
		campo = campo.replace(".", "");
		int soma = 0;
		int resultado = 0;

		for (char numero : campo.toCharArray()) {
			resultado = Character.getNumericValue(numero) * (valor ? 2 : 1);
			soma += resultado <= 9 ? resultado : (resultado - 10) + 1;
			valor = !valor;
		}

		int proximaDezena = ((soma / 10) + 1) * 10;

		int dvCampo = (proximaDezena - (proximaDezena - 10 + soma));

		if (dvCampo >= 10) {
			return 1;
		}

		return dvCampo;

	}

	/**
	 * Calcula o digito verificado do c�digo de barras
	 * 
	 * @param codigoBarras
	 * @return
	 */
	protected int digitoVerificadorCodigoBarras(String codigoBarras) {
		// TODO: COMPLETAR
		return 0;
	}

	/**
	 * Campo 1 da Linha Digit�vel
	 * 
	 * - C�digo do Banco - C�digo da Moeda - N�mero do conv�nio
	 * 
	 * @return
	 */
	private String ldCampo1() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(getLDNumeroConvenio());
		return buffer.toString();

	}

	public String getLinhaDigitavel() {

		init();

		StringBuilder buffer = new StringBuilder();
		buffer.append(ldCampo1());
		buffer.append(digitoVerificadorPorCampo(ldCampo1(), true));
		buffer.append(" ");

		buffer.append(ldCampo2());
		buffer.append(digitoVerificadorPorCampo(ldCampo2(), false));
		buffer.append(" ");

		buffer.append(ldCampo3());
		buffer.append(digitoVerificadorPorCampo(ldCampo3(), false));
		buffer.append(" ");

		buffer.append(ldCampo4());
		buffer.append(" ");

		buffer.append(ldCampo5());

		return buffer.toString();
	}

	/**
	 * Retorna a diferen�a em dias de duas datas
	 * 
	 * @param dataInicial
	 *            Data inicial
	 * @param dataFinal
	 *            Data final
	 * @return
	 */
	protected static long diferencaEmDias(Date dataInicial, Date dataFinal) {

		// Estude a Math e escreva aqui o que este m�todo est� fazendo

		/**
		 * Esse m�todo mostra a diferen�a em dias pegando a data final,
		 * subtraindo da data final e dividindo por 86400000D(um dia) E retorna
		 * o valor da diferen�a 86400000D � igual a 1 dia
		 */
		return Math
				.round((dataFinal.getTime() - dataInicial.getTime()) / 86400000D);

	}

	public int getDvCodigoBarras() {

		getCodigoBarras();

		return dvCodigoBarras;
	}
}
