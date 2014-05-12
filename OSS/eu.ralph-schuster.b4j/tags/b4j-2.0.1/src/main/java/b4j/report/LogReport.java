/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package b4j.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Issue;

/**
 * @author Ralph Schuster
 *
 */
public class LogReport extends AbstractReportGenerator {

	private static Logger log = LoggerFactory.getLogger(LogReport.class);
	
	private int count = 0;
	/**
	 * 
	 */
	public LogReport() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeReport() {
		log.info(count+" bugs found");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBug(Issue bug) {
		log.info(bug.getId()+": "+bug.getStatus()+" "+bug.getPriority()+" "+bug.getResolution()+" "+bug.getSummary());
		count++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareReport() {
		count = 0;
		super.prepareReport();
	}

	
}
