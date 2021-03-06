/* Copyright (C) 2006  Versant Inc.  http://www.db4o.com */

package com.db4o.db4ounit.common.defragment;

import db4ounit.extensions.*;

public class AllTests extends Db4oTestSuite {
	
	public static void main(String[] args) {
		new AllTests().runAll();
    }

	protected Class[] testCases() {
		return new Class[] {
			BlockSizeDefragTestCase.class,
			CommitTimestampDefragmentTestCase.class,
			DefragEncryptedFileTestCase.class,
			DefragInheritedFieldIndexTestCase.class,
			DefragInMemoryTestSuite.class,
			DefragmentUntypedPrimitiveArrayTestCase.class,
			IdMappingTestSuite.class,
			RemovedFieldDefragmentTestCase.class,
			SlotDefragmentTestCase.class,
			StoredClassFilterTestCase.class,
			TranslatedDefragTestCase.class,
		};
	}
}
