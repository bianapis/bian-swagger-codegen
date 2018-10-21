package org.bian.swaggercodegen;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.bian.swaggercodegen.cmd.Generate;
import org.bian.swaggercodegen.generator.JavaBianCodegen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.airlift.airline.Cli;

// TODO: Auto-generated Javadoc
/**
 * User: lanwen Date: 24.03.15 Time: 17:56
 * <p>
 * Command line interface for swagger codegen use `swagger-codegen-cli.jar help`
 * for more info
 *
 * @since 2.1.3-M1
 */
public class BianCodegen {

	static Logger LOGGER = LoggerFactory.getLogger(BianCodegen.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		showBanner();
		@SuppressWarnings("unchecked")
		Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("swagger-codegen-cli").withCommands(Generate.class);

		builder.build().parse(args).run();

		LOGGER.info("Done!");
	}

	private static void showBanner() {
		System.out.println();
		System.out.println(" ________    ____ _____          _   _ ");
		System.out.println("|\\\\`'·--'|  |  _ \\_   _|   /\\   | \\ | |");
		System.out.println("|\\\\`'·._·|  | |_) || |    /  \\  |  \\| |");
		System.out.println("|·\\`'·._·|  |  _ < | |   / /\\ \\ | . ` |");
		System.out.println("| \\`·.__·|  | |_) || |_ / ____ \\| |\\  |");
		System.out.println("|________|  |____/_____/_/    \\_\\_| \\_|");
		System.out.println();
		System.out.println("             Banking Industry               ");
		System.out.println("             Architecture Network");
		System.out.println();
		System.out.println("        -----------------------");
		System.out.println("         Powered by Virtusa(c)");
		System.out.println("     -----------------------------");
		System.out.println("      BIAN Swagger Code Generator");
		System.out.println("     -----------------------------");
	}
}
