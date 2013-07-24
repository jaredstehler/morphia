/**
 * Copyright (C) 2010 Olafur Gauti Gudmundsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.code.morphia;


import org.junit.Test;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.mongodb.WriteConcern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Scott Hernandez
 */
public class TestAnnotatedWriteConcern extends TestBase {

    @Entity(concern = "Safe")
    static class Simple {
        @Id
        String id;

        public Simple(final String id) {
            this();
            this.id = id;
        }

        private Simple() {
        }
    }

    @Test
    public void defaultWriteConcern() throws Exception {
        boolean failed = false;
        final AdvancedDatastore aDs = (AdvancedDatastore) ds;
        try {
            aDs.insert(new Simple("simple"), ds.getDefaultWriteConcern());
            aDs.insert(new Simple("simple"), ds.getDefaultWriteConcern());
        } catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was raised!", failed);
    }

    @Test
    public void safeWriteConcern() throws Exception {
        boolean failed = false;
        final AdvancedDatastore aDs = (AdvancedDatastore) ds;
        try {
            aDs.insert(new Simple("simple"));
            aDs.insert(new Simple("simple"), WriteConcern.SAFE);
        } catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was raised!", failed);
    }

    @Test
    public void noneWriteConcern() throws Exception {
        boolean failed = false;
        final AdvancedDatastore aDs = (AdvancedDatastore) ds;
        ds.setDefaultWriteConcern(WriteConcern.NONE);
        try {
            aDs.insert(new Simple("simple"));
            aDs.insert(new Simple("simple"));
        } catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was raised!", failed);
    }
}
