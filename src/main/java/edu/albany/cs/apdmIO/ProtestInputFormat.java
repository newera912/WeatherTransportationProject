package edu.albany.cs.apdmIO;

import java.io.File;

public class ProtestInputFormat extends APDMInputFormat {

	public ProtestInputFormat(File APDMFile) {
		super(APDMFile);
	}

	public ProtestInputFormat(String APDMFile) {
		this(new File(APDMFile));
	}

	public static void convertProtestData2APDM() {

	}

	public static void main(String args[]) {

	}

}
