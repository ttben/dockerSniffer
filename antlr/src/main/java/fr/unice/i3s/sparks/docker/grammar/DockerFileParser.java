package fr.unice.i3s.sparks.docker.grammar;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.unice.i3s.sparks.docker.core.commands.*;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.ImageID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DockerFileParser {

    public static final Pattern FROM_PATTERN = Pattern.compile("[fF][rR][oO][mM](\\s)+[^\\n\\s]+");

    public static final Pattern ENV_PATTERN = Pattern.compile("[eE][nN][vV](\\s)+[^\\s]+(\\s)*[^\\s]+");

    public static final Pattern CMD_PATTERN = Pattern.compile("[cC][mM][dD](\\s)+(.)+");
    public static final Pattern COPY_PATTERN = Pattern.compile("[cC][oO][pP][yY](\\s)+(.)+(\\s)+(.)+");
    public static final Pattern ADD_PATTERN = Pattern.compile("[aA][dD][dD](\\s)+(.)+(\\s)+(.)+");

    public static final Pattern ENTRYPOINT_PATTERN = Pattern.compile("[eE][nN][tT][rR][yY][pP][oO][iI][nN][tT](\\s)+(.)+");
    public static final Pattern EXPOSE_PATTERN = Pattern.compile("[eE][xX][pP][oO][sS][eE](\\s)+(.)+");

    public static final Pattern MAINTAINER_PATTERN = Pattern.compile("[mM][aA][iI][nN][tT][aA][iI][nN][eE][rR](\\s)+(.)+");
    public static final Pattern WORKDIR_PATTERN = Pattern.compile("[wW][oO][rR][kK][dD][iI][rR](\\s)+(.)+");
    public static final Pattern VOLUME_PATTERN = Pattern.compile("[vV][oO][lL][uU][mM][eE](\\s)+(.)+");
    public static final Pattern USER_PATTERN = Pattern.compile("[uU][sS][eE][rR](\\s)+(.)+");

    public static final Pattern RUN_PATTERN_ONELINE = Pattern.compile("[rR][uU][nN](\\s)+([^\\n\\\\])+");
    public static final Pattern RUN_PATTERN_MUTLILINE = Pattern.compile("[rR][uU][nN](\\s)+([^\\n\\\\])*\\\\");
    public static final Pattern RUN_AGAIN_MULTILINE = Pattern.compile("([^\\\\])+\\\\");
    public static final Pattern RUN_END_MULTILINE = Pattern.compile("([^\\\\\n])+");

    public static Dockerfile parse(File file) throws IOException {
        ArrayList<String> lines = Files.lines(Paths.get(file.getAbsolutePath())).collect(Collectors.toCollection(ArrayList::new));

        Dockerfile result = new Dockerfile();

        ListIterator<String> stringListIterator = lines.listIterator();


        while (stringListIterator.hasNext()) {
            String line = stringListIterator.next();

            try {
                line = line.replace('\t', ' ');
                line = line.replaceAll("(\\s)+", " ");
                line = line.trim();

                if (line.trim().startsWith("#") | line.trim().isEmpty()) {
                    continue;
                }

                if (FROM_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    FROMCommand fromCommand = new FROMCommand(new ImageID(body));
                    result.addCommand(fromCommand);
                    continue;
                }

                if (WORKDIR_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    WORKDIRCommand workdirCommand = new WORKDIRCommand(body);
                    result.addCommand(workdirCommand);
                    continue;
                }

                if (VOLUME_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    VOLUMECommand volumeCommand = new VOLUMECommand(body);
                    result.addCommand(volumeCommand);
                    continue;
                }

                if (USER_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    USERCommand userCommand = new USERCommand(body);
                    result.addCommand(userCommand);
                    continue;
                }

                if (MAINTAINER_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);
                    MAINTAINERCommand maintainerCommand = new MAINTAINERCommand(body);
                    result.addCommand(maintainerCommand);
                    continue;
                }

                if (ENV_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);


                    String descBody = "";

                    for (String s : body) {
                        descBody += s;
                    }

                    String key, value;

                    // ENV a=b
                    if (body.length == 1 && descBody.contains("=")) {
                        body = descBody.split("=");
                        key = body[0];
                        value = body[1];
                    } else if (body.length > 1 && !descBody.contains("=")) {
                        key = body[0];
                        value = "";
                    } else {
                        key = body[0];
                        value = body[1];
                    }

                    ENVCommand envCommand = new ENVCommand(key, value);
                    result.addCommand(envCommand);
                    continue;
                }

                if (CMD_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    CMDCommand cmdCommand = new CMDCommand(body);
                    result.addCommand(cmdCommand);
                    continue;
                }


                if (ADD_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    ADDCommand addCommand = new ADDCommand(body);
                    result.addCommand(addCommand);
                    continue;
                }

                if (COPY_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    COPYCommand copyCommand = new COPYCommand(body);
                    result.addCommand(copyCommand);
                    continue;
                }

                if (ENTRYPOINT_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    ENTRYPointCommand entryPointCommand = new ENTRYPointCommand(body);
                    result.addCommand(entryPointCommand);
                    continue;
                }

                if (EXPOSE_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    EXPOSECommand exposeCommand = new EXPOSECommand(body);
                    result.addCommand(exposeCommand);
                    continue;
                }

                if (RUN_PATTERN_ONELINE.matcher(line).matches()) {
                    List<ShellCommand> shellCommands = buildRunCommand(line, true);
                    RUNCommand runCommand = new RUNCommand(shellCommands);
                    result.addCommand(runCommand);
                    continue;
                }
                if (RUN_PATTERN_MUTLILINE.matcher(line).matches()) {
                    List<ShellCommand> shellCommands = buildRunCommand(line, true);
                    line = stringListIterator.next();

                    while (RUN_AGAIN_MULTILINE.matcher(line).matches()) {
                        shellCommands.addAll(buildRunCommand(line, false));
                        line = stringListIterator.next();
                    }

                    if (RUN_END_MULTILINE.matcher(line).matches()) {
                        shellCommands.addAll(buildRunCommand(line, false));
                    }
                    continue;
                }

                result.addCommand(new NonParsedCommand(line));
            } catch (NoSuchElementException | IndexOutOfBoundsException e) {
                System.err.println("Parse error on file:" + file.getAbsolutePath());
                result.addCommand(new NonParsedCommand(line));

            }
        }


        return result;
    }

    private static List<ShellCommand> buildRunCommand(String line, boolean isFirstLineOfRun) {
        String[] split = line.split("&&");

        if (isFirstLineOfRun) {
            split[0] = split[0].substring(4);
        }

        List<ShellCommand> shellCommands = new ArrayList<>();
        for (String s : split) {
            s = s.trim();
            ShellCommand shellCommand = new ShellCommand(s.split(" "));
            shellCommands.add(shellCommand);
        }

        return shellCommands;
    }

    public static void main(String[] args) {

        Matcher m = ENV_PATTERN.matcher("ENV PATH=PATH");
        System.out.println(m.matches());

        m = ENV_PATTERN.matcher("ENV PATH :PATH");
        System.out.println(m.matches());

        /*
        String s = "FROM haskell:7.8.4\n" +
                "\n" +
                "RUN apt-get update && \\\n" +
                "    apt-get install -y --no-install-recommends libpq5 libpq-dev &&\\\n" +
                "    apt-get clean\n" +
                "\n";
        Matcher m = RUN_PATTERN_ONELINE.matcher(s);
        System.out.println(m.matches());

        m = RUN_PATTERN_MUTLILINE.matcher(s);
        System.out.println(m.matches());


        System.out.println("↓FALSE↓");
        Matcher m = RUN_PATTERN_ONELINE.matcher("RUN ");
        System.out.println(m.matches());

        m = RUN_PATTERN_ONELINE.matcher("RUNe apt-get update && install -y");
        System.out.println(m.matches());

        m = RUN_PATTERN_ONELINE.matcher("RUNq apt-get update \\\n && install -y");
        System.out.println(m.matches());

        System.out.println("↓TRUE↓");

        m = RUN_PATTERN_ONELINE.matcher("RUN apt-get update && install -y");
        System.out.println(m.matches());

        m = RUN_PATTERN_MUTLILINE.matcher("RUN apt-get update sgfsgsv.,l gy deguyhdb afgq==t356=-0=-fgv dsfb\\\n");
        System.out.println(m.matches());


        System.out.println("↓FALSE↓");
        m = FROM_PATTERN.matcher("FROM");
        System.out.println(m.matches());

        m = FROM_PATTERN.matcher("FROM ");
        System.out.println(m.matches());

        m = FROM_PATTERN.matcher("FROM  ");
        System.out.println(m.matches());

        m = FROM_PATTERN.matcher("FROM   ");
        System.out.println(m.matches());

        System.out.println("↓TRUE↓");

        m = FROM_PATTERN.matcher("FROM a:a");
        System.out.println(m.matches());

        m = FROM_PATTERN.matcher("FROM a/a");
        System.out.println(m.matches());

        System.out.println("↓FALSE↓");

        m = ENV_PATTERN.matcher("ENV");
        System.out.println(m.matches());

        m = ENV_PATTERN.matcher("ENV ");
        System.out.println(m.matches());

        m = ENV_PATTERN.matcher("ENV  ");
        System.out.println(m.matches());

        m = ENV_PATTERN.matcher("ENV   ");
        System.out.println(m.matches());

        System.out.println("↓TRUE↓");

        m = ENV_PATTERN.matcher("ENV a a");
        System.out.println(m.matches());

        m = ENV_PATTERN.matcher("ENV a=a");
        System.out.println(m.matches());
        */
    }
}