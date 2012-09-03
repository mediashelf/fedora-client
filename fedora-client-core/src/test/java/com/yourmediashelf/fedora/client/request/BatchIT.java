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
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

@SuppressWarnings("deprecation")
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-lists")
@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 5)
//@BenchmarkHistoryChart(labelWith = LabelType.RUN_ID, maxRuns = 20)
abstract public class BatchIT extends BaseFedoraRequestIT {

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private static String pidNamespace = "test-batch";

    private static Set<String> pids = new HashSet<String>();

    private static Set<String> dsids = new HashSet<String>();

    @BeforeClass
    public static void createFixtures() throws FedoraClientException {

        int[] dsSizes = {10, 100, 1000};

        //String[] dsTypes = {"X", "M"};
        String[] dsTypes = {"M"};

        int pidCountPerDsType = 20;

        String pid, dsid;

        Map<String, String> contents = new HashMap<String, String>();
        for (int dsSize : dsSizes) {
            contents.put(String.valueOf(dsSize), "<foo>" +
                    createDataSize(dsSize) + "</foo>");
        }

        System.out.print("Creating fixture objects, this may take some time ");
        FedoraRequest.setDefaultClient(fedora);
        for (String dsType : dsTypes) {
            for (int i = 1; i <= pidCountPerDsType; i++) {
                pid = String.format("%s:%s%d", pidNamespace, dsType, i); // e.g. test-batch:X1

                pids.add(pid);
                ingest(pid).execute();
                System.out.print(".");

                for (int size : dsSizes) {
                    dsid = String.format("%s%sK", dsType, size); // e.g. X100K or M10K
                    addDatastream(pid, dsid).controlGroup(dsType).content(
                            contents.get(String.valueOf(size))).execute();

                    dsids.add(dsid);
                }
            }
        }
        System.out.println();

        System.out.println("* Ingested pids:\n\t" + listAsCSV(pids));
        System.out.println("* Added dsids:\n\t" + listAsCSV(dsids));
    }

    @AfterClass
    public static void purgeFixtures() throws FedoraClientException {
        System.out.print("Purging fixtures, this may take some time ");
        for (String pid : pids) {
            purgeObject(pid).execute(fedora);
            System.out.print(".");
        }
        System.out.println();
    }

    @Override
    @Before
    public void setUp() {
    }

    @Override
    @After
    public void tearDown() {
    }

    @Override
    @Ignore
    @Test
    public void testNoDefaultClientRequest() throws FedoraClientException {
    }

    protected void getBatchDatastreams(Set<String> pids, Set<String> dsids)
            throws Exception {
        BatchResponse response =
                new BatchGetDatastreams().pids(pids).dsids(dsids).execute();
        assertEquals(200, response.getStatus());

        List<BodyPart> bps = response.getBodyParts();
        for (BodyPart bp : bps) {
            bp.getEntityAs(String.class);
            bp.cleanup();
        }
        response.close();
    }

    protected void naiveGetDatastreams(Set<String> pids, Set<String> dsids)
            throws Exception {
        FedoraResponse response = null;
        for (String pid : pids) {
            for (String dsid : dsids) {
                response = getDatastreamDissemination(pid, dsid).execute();
                assertEquals(200, response.getStatus());
                response.getEntity(String.class);
                response.close();
            }
        }
    }

    protected static Set<String> matchIds(Collection<String> ids, String regex) {
        return matchIds(ids, regex, 0);
    }

    /**
     * Build a List of identifiers that match the provided regex.
     *
     */
    protected static Set<String> matchIds(Collection<String> ids, String regex,
            int limit) {
        Set<String> matchedIds = new HashSet<String>();
        for (String id : ids) {
            if (id.matches(regex)) {
                matchedIds.add(id);
                if (matchedIds.size() == limit) {
                    break;
                }
            }
        }
        return matchedIds;
    }

    protected static Set<String> getManagedPids(int limit) {
        return matchIds(pids, "test-batch:M.*", limit);
    }

    protected static Set<String> getDsids(String regex) {
        return matchIds(dsids, regex);
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

    private static String listAsCSV(Collection<String> list) {
        String separator = ",";
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(separator);
        }
        return sb.toString().substring(0, sb.length() - separator.length());
    }
}
