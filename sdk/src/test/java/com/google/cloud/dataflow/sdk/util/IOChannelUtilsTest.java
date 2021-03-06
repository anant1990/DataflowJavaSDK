/*
 * Copyright (C) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataflow.sdk.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.nio.channels.WritableByteChannel;

/**
 * Tests for IOChannelUtils.
 */
@RunWith(JUnit4.class)
public class IOChannelUtilsTest {
  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Test
  public void testShardFormatExpansion() {
    Assert.assertEquals("output-001-of-123.txt",
        IOChannelUtils.constructName("output", "-SSS-of-NNN",
            ".txt",
            1, 123));

    Assert.assertEquals("out.txt/part-00042",
        IOChannelUtils.constructName("out.txt", "/part-SSSSS", "",
            42, 100));

    Assert.assertEquals("out.txt",
        IOChannelUtils.constructName("ou", "t.t", "xt", 1, 1));

    Assert.assertEquals("out0102shard.txt",
        IOChannelUtils.constructName("out", "SSNNshard", ".txt", 1, 2));

    Assert.assertEquals("out-2/1.part-1-of-2.txt",
        IOChannelUtils.constructName("out", "-N/S.part-S-of-N",
            ".txt", 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShardNameCollision() throws Exception {
    File outFolder = tmpFolder.newFolder();
    String filename = outFolder.toPath().resolve("output").toString();

    WritableByteChannel output = IOChannelUtils
        .create(filename, "", "", 2, "text");
    Assert.fail("IOChannelUtils.create expected to fail due "
        + "to filename collision");
  }

  @Test
  public void testLargeShardCount() {
    Assert.assertEquals("out-100-of-5000.txt",
        IOChannelUtils.constructName("out", "-SS-of-NN", ".txt",
            100, 5000));
  }
}
