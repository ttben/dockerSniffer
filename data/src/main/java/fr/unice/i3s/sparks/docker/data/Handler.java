package fr.unice.i3s.sparks.docker.data;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Handler {
    private URL pathToFile;

    public Handler(URL pathToFile) {

        this.pathToFile = pathToFile;
    }

    public void start() throws IOException {

        String folderThatWillContainsDockerfiles = "./src/main/resources/dockerfiles";

        Reader in = new FileReader(pathToFile.getPath());
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withSkipHeaderRecord()
                .withHeader("page","page-href","linkToDockerfile")
                .withDelimiter(';').parse(in);

        int nb = 0;
        int newRecords = 0;
        for (CSVRecord record : records) {
            nb++;
            //System.out.println(record);
            String linkToDockerfile = record.get("linkToDockerfile");
            System.out.println(linkToDockerfile);

            String fileName  = linkToDockerfile.replace("/", "_");
            fileName = fileName.replace(":", "-");
            fileName = fileName.replace("%", "__");

            File f = new File(folderThatWillContainsDockerfiles + "/" + fileName + "-dockerfile");
            if(f.exists()) {
                continue;
            }

            newRecords++;

            String[] split = linkToDockerfile.split("/");
            String res = "";
            for(int i = 0 ; i < split.length ; i++) {
                if(i != 3) {
                    res += split[i] + "/";
                }
            }

            res = res.substring(0, res.length() - 1);

            res = "http://raw.githubusercontent.com" + res;

            HttpGet httpget = new HttpGet(res);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    OutputStream fileOutputStream= new FileOutputStream(f);

                    String header = "#  Dockerfile retrieved from:\n";
                    //header += "#    Repository: "+ record.get("repo") + "\n";
                    header += "#    Dockerfile: "+ res + "\n";
                    fileOutputStream.write(header.getBytes());

                    IOUtils.copy(instream, fileOutputStream);
                }
            } finally {
                response.close();
            }


            System.out.println(res);
        }

        System.out.println("Nb records:" + nb);
        System.out.println("Nb new records:" + newRecords);

    }

    public static void main(String[] args) throws IOException {
        Handler handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_25-11-16-all.csv"));
        handler.start();
    }
}
