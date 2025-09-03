package projeto.System.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SQLreader {
    
    public static String carregarArquivo(final String arquivoNome) throws IOException {

        InputStream insArquivo = SQLreader.class.getResourceAsStream(arquivoNome);
        InputStreamReader insLer = new InputStreamReader(insArquivo, StandardCharsets.UTF_8);
        BufferedReader leitor = new BufferedReader(insLer);
        StringBuilder stringBuild = new StringBuilder();
        String linha;

        while ((linha = leitor.readLine()) != null) {
            stringBuild.append(linha).append("\n");
        }

        leitor.close();

        return stringBuild.toString();

    }

}
