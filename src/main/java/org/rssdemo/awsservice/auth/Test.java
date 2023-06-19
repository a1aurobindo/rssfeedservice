//package org.rssdemo.awsservice.auth;
//
//// snippet-sourcedescription:[ ]
//// snippet-service:[dynamodb]
//// snippet-keyword:[Java]
//// snippet-sourcesyntax:[java]
//// snippet-keyword:[Amazon DynamoDB]
//// snippet-keyword:[Code Sample]
//// snippet-keyword:[ ]
//// snippet-sourcetype:[full-example]
//// snippet-sourcedate:[ ]
//// snippet-sourceauthor:[AWS]
//// snippet-start:[dynamodb.java.trydax.TryDaxHelper]
//
///**
// * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// *
// * This file is licensed under the Apache License, Version 2.0 (the "License").
// * You may not use this file except in compliance with the License. A copy of
// * the License is located at
// *
// * http://aws.amazon.com/apache2.0/
// *
// * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// * CONDITIONS OF ANY KIND, either express or implied. See the License for the
// * specific language governing permissions and limitations under the License.
// */
//import java.util.Arrays;
//
//import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;
//
//public class TryDaxHelper {
//
//    private static final String region = EC2MetadataUtils.getToken();
//
//
//
//    void createTable(String tableName, DynamoDB client) {
//        Table table = client.getTable(tableName);
//        try {
//            System.out.println("Attempting to create table; please wait...");
//
//            table = client.createTable(tableName,
//                    Arrays.asList(
//                            new KeySchemaElement("pk", KeyType.HASH),   // Partition key
//                            new KeySchemaElement("sk", KeyType.RANGE)), // Sort key
//                    Arrays.asList(
//                            new AttributeDefinition("pk", ScalarAttributeType.N),
//                            new AttributeDefinition("sk", ScalarAttributeType.N)),
//                    new ProvisionedThroughput(10L, 10L));
//            table.waitForActive();
//            System.out.println("Successfully created table.  Table status: " +
//                    table.getDescription().getTableStatus());
//
//        } catch (Exception e) {
//            System.err.println("Unable to create table: ");
//            e.printStackTrace();
//        }
//    }
//
//    void writeData(String tableName, DynamoDB client, int pkmax, int skmax) {
//        Table table = client.getTable(tableName);
//        System.out.println("Writing data to the table...");
//
//        int stringSize = 1000;
//        StringBuilder sb = new StringBuilder(stringSize);
//        for (int i = 0; i < stringSize; i++) {
//            sb.append('X');
//        }
//        String someData = sb.toString();
//
//        try {
//            for (Integer ipk = 1; ipk <= pkmax; ipk++) {
//                System.out.println(("Writing " + skmax + " items for partition key: " + ipk));
//                for (Integer isk = 1; isk <= skmax; isk++) {
//                    table.putItem(new Item()
//                            .withPrimaryKey("pk", ipk, "sk", isk)
//                            .withString("someData", someData));
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Unable to write item:");
//            e.printStackTrace();
//        }
//    }
//
//    void deleteTable(String tableName, DynamoDB client) {
//        Table table = client.getTable(tableName);
//        try {
//            System.out.println("\nAttempting to delete table; please wait...");
//            table.delete();
//            table.waitForDelete();
//            System.out.println("Successfully deleted table.");
//
//        } catch (Exception e) {
//            System.err.println("Unable to delete table: ");
//            e.printStackTrace();
//        }
//    }
//
//}
//
//// snippet-end:[dynamodb.java.trydax.TryDaxHelper]
