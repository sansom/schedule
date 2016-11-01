/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package org.quartz.examples.example6;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob1 implements Job {

  // Logging
  private static Logger _log = LoggerFactory.getLogger(BadJob1.class);
  private int calculation;

  /**
   * Empty public constructor for job initialization
   */
  public BadJob1() {
  }

  /**
   * <p>
   * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that
   * is associated with the <code>Job</code>.
   * </p>
   * 
   * @throws JobExecutionException if there is an exception while executing the job.
   */
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobKey jobKey = context.getJobDetail().getKey();
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();

    int denominator = dataMap.getInt("denominator");
    _log.info("---" + jobKey + " executing at " + new Date() + " with denominator " + denominator);

    try {
      calculation = 4815 / denominator;
    } catch (Exception e) {
      _log.info("--- Error in job!");
      JobExecutionException e2 = new JobExecutionException(e);
      
      //修复错误
      dataMap.put("denominator", "1");

      //设置马上重新执行
      e2.setRefireImmediately(true);
      throw e2;
    }

    _log.info("---" + jobKey + " completed at " + new Date());
  }

}