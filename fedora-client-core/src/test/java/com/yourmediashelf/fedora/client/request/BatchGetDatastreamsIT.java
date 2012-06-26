/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.yourmediashelf.fedora.client.request;
/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */


import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastreamDissemination;
import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeObject;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.sun.jersey.multipart.BodyPart;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.BatchResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 5)
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-lists")
@Ignore("Batch API is not included by default in Fedora")
public class BatchGetDatastreamsIT extends BaseFedoraRequestIT {

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private static String[] dsids = {"FOO", "BAR", "BAZ"};

    private static int count = 10;

    private static String[] pids10k = new String[count];

    private static String[] pids100k = new String[count];

    private static String[] pids1000k = new String[count];

    @BeforeClass
    public static void createFixtures() throws Exception {
        FedoraRequest.setDefaultClient(fedora);

        ingestObjects(pids10k, 10);
        ingestObjects(pids100k, 100);
        ingestObjects(pids1000k, 1000);
    }

    @AfterClass
    public static void purgeFixtures() throws Exception {
        purgeObjects(pids10k);
        purgeObjects(pids100k);
        purgeObjects(pids1000k);

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test10KBatch() throws Exception {
        getBatchDatastreams(pids10k);
    }

    @Test
    public void test100KBatch() throws Exception {
        getBatchDatastreams(pids100k);
    }

    @Test
    public void test1000KBatch() throws Exception {
        getBatchDatastreams(pids1000k);
    }

    @Test
    public void testNaive10K() throws Exception {
        naiveGetDatastreams(pids10k);
    }

    @Test
    public void testNaive100K() throws Exception {
        naiveGetDatastreams(pids100k);
    }

    @Test
    public void testNaive1000K() throws Exception {
        naiveGetDatastreams(pids1000k);
    }
    
    @Test(expected=FedoraClientException.class)
    public void testBadBatch() throws Exception {
        String[] pids = {pids10k[0], "test-batch:thisPidDoesntExist", pids100k[0]};
        getBatchDatastreams(pids);
    }

    @Ignore @Test
    public void testNoDefaultClientRequest() throws FedoraClientException {
    }

    private void getBatchDatastreams(String[] pids) throws Exception {
        BatchResponse response =
                new BatchGetDatastreams().dsids(dsids).pids(pids).execute();       
        List<BodyPart> bps = response.getBodyParts();
        for (BodyPart bp : bps) {
            bp.getEntityAs(String.class);
            bp.cleanup();
        }
        response.close();
    }

    private void naiveGetDatastreams(String[] pids) throws Exception {
        FedoraResponse response = null;
        for (String pid : pids) {
            for (String dsid : dsids) {
                response = getDatastreamDissemination(pid, dsid).execute();
                response.getEntity(String.class);
            }
        }
    }

    /**
     * Create a String of the requested size (in kilobytes).
     * 
     * @param msgSize size in kilobytes
     * @return a String of 'a's
     */
    private static String createDataSize(int msgSize) {
        // Java chars are 2 bytes
        msgSize = msgSize / 2;
        msgSize = msgSize * 1024;
        StringBuilder sb = new StringBuilder(msgSize);
        for (int i = 0; i < msgSize; i++) {
            sb.append('a');
        }
        return sb.toString();
    }

    private static void ingestObjects(String[] pids, int size) throws Exception {
        String namespace = "test-batch-" + size + "k";
        String pid;

        String content = "<foo>" + createDataSize(size) + "</foo>";

        for (int j = 0; j < count; j++) {
            pid = namespace + ":" + j;
            pids[j] = pid;
            ingest(pid).execute();
            for (String dsid : dsids) {
                addDatastream(pid, dsid).controlGroup("M").content(content)
                        .execute();
            }
        }
    }

    private static void purgeObjects(String[] pids) throws Exception {
        for (String pid : pids) {
            purgeObject(pid).execute(fedora);
        }
    }
}
