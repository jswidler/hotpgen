package com.jswidler.hotpgen;

import com.jswidler.hotpgen.app.HotpGenMain;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.security.GeneralSecurityException;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException {
        HotpGenArguments commandLineArgs = parseCommandLine(args);

        HotpGenMain app = new HotpGenMain(commandLineArgs.key, commandLineArgs.password);
        app.run();
    }


    /*
     * Command line parsing
     */

    private static class HotpGenArguments {
        String key = null;
        String password = null;
    }

    private static HotpGenArguments parseCommandLine(String[] args) {
        Options options = new Options();
        options.addOption("k", "key", true, "the key (provide once - it is encrypted in properties file)");
        options.addOption("p", "password", true, "optional password for stronger encryption in the properties file");

        HotpGenArguments commandLineArgs = new HotpGenArguments();

        try {
            CommandLine cmd = new BasicParser().parse(options, args);
            if (cmd.getArgs().length != 0) {
                throw new ParseException("Unrecognized argument: " + cmd.getArgs()[0]);
            }
            commandLineArgs.key = cmd.getOptionValue("k");
            commandLineArgs.password = cmd.getOptionValue("p");
        } catch (ParseException e) {
            System.out.println("Parsing command line failed - " + e.getMessage() );
            new HelpFormatter().printHelp("hotpgen", options);
        }

        return commandLineArgs;
    }
}
