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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Handler {
    private URL pathToFile;

    public Handler(URL pathToFile) {
        this.pathToFile = pathToFile;
    }

    public static void main(String[] args) throws IOException {
        Handler handler = new Handler(Handler.class.getClassLoader().getResource("github-crawl-result15-11-16.csv"));
        handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_21-11-16.csv"));
        handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_21-11-16_2.csv"));
         handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_21-11-16_3.csv"));
        handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_21-11-16_4.csv"));
        handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_24-11-16.csv"));
        handler.start();

        handler = new Handler(Handler.class.getClassLoader().getResource("github_docker_25-11-16-all.csv"));
        handler.start();
    }

    public void start() throws IOException {

        String folderThatWillContainsDockerfiles = buildTargetFolder();

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
            System.out.println("+"+ linkToDockerfile);

            String fileName  = linkToDockerfile.replace("/", "_");
            fileName = fileName.replace(":", "-");
            fileName = fileName.replace("%", "__");

            String pathname = folderThatWillContainsDockerfiles + "/" + fileName + "-dockerfile";
            File f = new File(pathname);
            if(f.exists()) {
                continue;
            } else {
                System.out.println("+ New file!");
                f.createNewFile();
            }

            newRecords++;

            downloadDockerfile(linkToDockerfile, f);
        }

        System.out.println("Nb records:" + nb);
        System.out.println("Nb new records:" + newRecords);
    }

    private String downloadDockerfile(String linkToDockerfile, File f) throws IOException {
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
        return res;
    }

    private String buildTargetFolder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("src");
        stringBuilder = stringBuilder.append(File.separator);
        stringBuilder = stringBuilder.append("main");
        stringBuilder = stringBuilder.append(File.separator);
        stringBuilder = stringBuilder.append("resources");
        stringBuilder = stringBuilder.append(File.separator);
        stringBuilder = stringBuilder.append("dockerfiles");
        stringBuilder = stringBuilder.append(File.separator);

        String folderThatWillContainsDockerfiles = stringBuilder.toString();

        File targetDirectory = new File(folderThatWillContainsDockerfiles);

        if( ! (targetDirectory.exists() && targetDirectory.isDirectory())) {
            System.out.println("Target directory did not exists, building it... :" + targetDirectory.getAbsolutePath());
            boolean mkdirs = targetDirectory.mkdirs();
            if(! mkdirs) {
                System.err.println("Can not create target directory...");
            } else {
                System.out.println("Target directory built!");
            }
        } else {
            System.out.println("Target directory already exists");
        }
        return folderThatWillContainsDockerfiles;
    }

}
