package br.com.banco.api.utils;

import java.text.DecimalFormat;

public class CPFCNPJUtils {

    private static final String VALUE_CANNOT_BE_NULL = "Não pode ser nulo";
    private static final String VALUE_CANNOT_BE_NULL_OR_EMPTY = "Não pode ser nulo ou vazio";
    private static final String SIZE_OF_VALUE_CANNOT_BE_BIGGER_THEN_14 = "O tamanho não pode ser maior que 14";
    private static final String VALUE_IS_NOT_A_VALID_CPF_OR_CPNJ = "Não é um CPF ou CNPJ válido";
    private static final DecimalFormat CNPJ_NFORMAT = new DecimalFormat(
            "00000000000000");
    private static final DecimalFormat CPF_NFORMAT = new DecimalFormat(
            "00000000000");
    private static final int[] weightCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] weightCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4,
            3, 2};

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00 caso o
     * mesmo seja um CPF ou CNPJ válido
     *
     * @param value [string] representa um CPF ou CNPJ
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(String value) {
        if (value == null || value.isEmpty())
            return value;

        return formatCPForCPNJ(Long.parseLong(value.replaceAll("[^0-9]+", "")),
                false);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00
     *
     * @param value [string] representa um CPF ou CNPJ
     * @param check [boolean] se true verifica se é um CPF ou CNPJ valido, se
     *              false apenas realiza a formatação
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(String value, boolean check) {
        if (value == null || value.isEmpty()) {
            if (check)
                throw new IllegalArgumentException(
                        VALUE_CANNOT_BE_NULL_OR_EMPTY);
            else
                return value;
        }

        return formatCPForCPNJ(Long.parseLong(value.replaceAll("[^0-9]+", "")),
                check);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00 caso o
     * mesmo seja um CPF ou CNPJ válido
     *
     * @param value [long] representa um CPF ou CNPJ
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(Long value) {
        if (value == null)
            return null;

        return formatCPForCPNJ(value, false);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00
     *
     * @param value [long] representa um CPF ou CNPJ
     * @param check [boolean] se true verifica se é um CPF ou CNPJ valido, se
     *              false apenas realiza a formatação
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(Long value, boolean check) {
        if (value == null) {
            if (check)
                throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL);
            else
                return null;
        }

        final int valueSize = value.toString().length();

        if (check) {
            if (valueSize > 14)
                throw new IllegalArgumentException(
                        SIZE_OF_VALUE_CANNOT_BE_BIGGER_THEN_14);

            if (!isCPForCPNJ(value))
                throw new IllegalArgumentException(
                        VALUE_IS_NOT_A_VALID_CPF_OR_CPNJ);
        }

        boolean isCPF = valueSize < 12;
        DecimalFormat formatDecimal = isCPF ? CPF_NFORMAT : CNPJ_NFORMAT;

        final String stringNumber = formatDecimal.format(value);

        return isCPF ? stringNumber.replaceAll(
                "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4")
                : stringNumber.replaceAll(
                "([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})",
                "$1.$2.$3/$4-$5");
    }

    /**
     * Verifica se um valor corresponde à um CPF ou CNPJ válido.
     *
     * @param value [string] valor à ser testado
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    public static boolean isCPForCPNJ(String value) {
        if (value == null || value.isEmpty())
            return false;

        return isCPForCPNJ(Long.parseLong(value.replaceAll("[^0-9]+", "")));
    }

    /**
     * Verifica se um valor corresponde à um CPF ou CNPJ válido.
     *
     * @param value [long] valor à ser testado
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    public static boolean isCPForCPNJ(Long value) {
        if (value == null)
            return false;

        final int valueSize = value.toString().length();

        if (valueSize > 14) {
            return false;
        }

        boolean isCPF = valueSize < 12;

        return isCPF ? isCPF(value) : isCNPJ(value);
    }

    /**
     * Verifica se um valor corresponde à um CPF.
     *
     * @param value [long] valor à ser testado
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    private static boolean isCPF(Long value) {
        if (value == null)
            return false;

        String CPF = CPF_NFORMAT.format(value);

        int firstPart = calcDigit(CPF.substring(0, 9), weightCPF);
        int lastPart = calcDigit(CPF.substring(0, 9) + firstPart, weightCPF);

        return CPF.substring(9).equals(
                String.format("%d%d", firstPart, lastPart));
    }

    /**
     * Verifica se um valor corresponde à um CNPJ.
     *
     * @param value [long] valor à ser testado
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    private static boolean isCNPJ(Long value) {
        if (value == null)
            return false;

        String CNPJ = CNPJ_NFORMAT.format(value);

        Integer firstPart = calcDigit(CNPJ.substring(0, 12), weightCNPJ);
        Integer lastPart = calcDigit(CNPJ.substring(0, 12) + firstPart,
                weightCNPJ);

        return CNPJ.substring(12).equals(
                String.format("%d%d", firstPart, lastPart));
    }

    /**
     * Calcula digito verificador para CPF or CPNJ
     *
     * @param stringBase [string] base do calculo do digito verificador
     * @param weight     array[int] representa os peso de cada caracter que compõe um
     *                   CPF ou CNPJ
     * @return [int] digito verificador
     */
    private static int calcDigit(String stringBase, int[] weight) {
        int sum = 0;
        for (int index = stringBase.length() - 1, digit; index >= 0; index--) {
            digit = Integer.parseInt(stringBase.substring(index, index + 1));
            sum += digit * weight[weight.length - stringBase.length() + index];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.length() != 14)
            return false;

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) { // CNPJ não possui somente números
            return false;
        }

        int soma = 0;
        String cnpj_calc = cnpj.substring(0, 12);

        char chr_cnpj[] = cnpj.toCharArray();
        for (int i = 0; i < 4; i++)
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));

        for (int i = 0; i < 8; i++)
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));

        int dig = 11 - soma % 11;
        cnpj_calc = new StringBuilder(String.valueOf(cnpj_calc)).append(
                dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
                .toString();
        soma = 0;
        for (int i = 0; i < 5; i++)
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));

        for (int i = 0; i < 8; i++)
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));

        dig = 11 - soma % 11;
        cnpj_calc = new StringBuilder(String.valueOf(cnpj_calc)).append(
                dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
                .toString();

        return cnpj.equals(cnpj_calc);
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null)
            return false;
        else {
            String cpfGerado = "";

            cpf = removerCaracteres(cpf);
            if (verificarSeTamanhoInvalido(cpf))
                return false;

            if (verificarSeDigIguais(cpf))
                return false;

            cpfGerado = cpf.substring(0, 9);
            cpfGerado = cpfGerado.concat(calculoComCpf(cpfGerado));
            cpfGerado = cpfGerado.concat(calculoComCpf(cpfGerado));

            if (!cpfGerado.equals(cpf))
                return false;
        }
        return true;
    }

    private static String removerCaracteres(String cpf) {
        return cpf.replace("-", "").replace(".", "").replace("/", "");
    }

    private static boolean verificarSeTamanhoInvalido(String cpf) {
        if (cpf.length() != 11)
            return true;
        return false;
    }

    private static boolean verificarSeDigIguais(String cpf) {
        // char primDig = cpf.charAt(0);
        char primDig = '0';
        char[] charCpf = cpf.toCharArray();
        for (char c : charCpf)
            if (c != primDig)
                return false;
        return true;
    }

    private static String calculoComCpf(String cpf) {
        int digGerado = 0;
        int mult = cpf.length() + 1;
        char[] charCpf = cpf.toCharArray();
        for (int i = 0; i < cpf.length(); i++)
            digGerado += (charCpf[i] - 48) * mult--;
        if (digGerado % 11 < 2)
            digGerado = 0;
        else
            digGerado = 11 - digGerado % 11;
        return String.valueOf(digGerado);
    }
}