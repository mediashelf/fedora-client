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

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

@Ignore("Batch API is not included by default in Fedora")
@BenchmarkMethodChart(filePrefix = "benchmark-mixed-lists")
public class BatchMixedIT extends BatchIT {

    private static Set<String> ds10k = getDsidsWithDC("M10K");

    private static Set<String> ds100k = getDsidsWithDC("M100K");

    private static Set<String> ds1000k = getDsidsWithDC("M1000K");

    @Test
    public void serial1x10k() throws Exception {
        int limit = 1;
        naiveGetDatastreams(getManagedPids(limit), ds10k);
    }

    @Test
    public void batch1x10k() throws Exception {
        int limit = 1;
        getBatchDatastreams(getManagedPids(limit), ds10k);
    }

    @Test
    public void serial10x10k() throws Exception {
        int limit = 10;
        naiveGetDatastreams(getManagedPids(limit), ds10k);
    }

    @Test
    public void batch10x10k() throws Exception {
        int limit = 10;
        getBatchDatastreams(getManagedPids(limit), ds10k);
    }

    @Test
    public void serial20x10k() throws Exception {
        int limit = 20;
        naiveGetDatastreams(getManagedPids(limit), ds10k);
    }

    @Test
    public void batch20x10k() throws Exception {
        int limit = 20;
        getBatchDatastreams(getManagedPids(limit), ds10k);
    }

    //
    @Test
    public void serial1x100k() throws Exception {
        int limit = 1;
        naiveGetDatastreams(getManagedPids(limit), ds100k);
    }

    @Test
    public void batch1x100k() throws Exception {
        int limit = 1;
        getBatchDatastreams(getManagedPids(limit), ds100k);
    }

    @Test
    public void serial10x100k() throws Exception {
        int limit = 10;
        naiveGetDatastreams(getManagedPids(limit), ds100k);
    }

    @Test
    public void batch10x100k() throws Exception {
        int limit = 10;
        getBatchDatastreams(getManagedPids(limit), ds100k);
    }

    @Test
    public void serial20x100k() throws Exception {
        int limit = 20;
        naiveGetDatastreams(getManagedPids(limit), ds100k);
    }

    @Test
    public void batch20x100k() throws Exception {
        int limit = 20;
        getBatchDatastreams(getManagedPids(limit), ds100k);
    }

    //
    @Test
    public void serial1x1000k() throws Exception {
        int limit = 1;
        naiveGetDatastreams(getManagedPids(limit), ds1000k);
    }

    @Test
    public void batch1x1000k() throws Exception {
        int limit = 1;
        getBatchDatastreams(getManagedPids(limit), ds1000k);
    }

    @Test
    public void serial10x1000k() throws Exception {
        int limit = 10;
        naiveGetDatastreams(getManagedPids(limit), ds1000k);
    }

    @Test
    public void batch10x1000k() throws Exception {
        int limit = 10;
        getBatchDatastreams(getManagedPids(limit), ds1000k);
    }

    @Test
    public void serial20x1000k() throws Exception {
        int limit = 20;
        naiveGetDatastreams(getManagedPids(limit), ds1000k);
    }

    @Test
    public void batch20x1000k() throws Exception {
        int limit = 20;
        getBatchDatastreams(getManagedPids(limit), ds1000k);
    }

    private static Set<String> getDsidsWithDC(String regex) {
        Set<String> s = getDsids(regex);
        s.add("DC");
        return s;
    }
}
