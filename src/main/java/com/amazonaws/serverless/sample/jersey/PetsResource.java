/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.serverless.sample.jersey;

import com.amazonaws.regions.Regions;
import com.amazonaws.serverless.sample.jersey.model.Pet;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.List;

@Path("/pets")
public class PetsResource {

    static AmazonDynamoDBClient client = new AmazonDynamoDBClient();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPet(final Pet newPet) {
        if (newPet.getName() == null || newPet.getBreed() == null) {
            return Response.status(400).entity(new Error("Invalid name or breed")).build();
        }

        client.withRegion(Regions.EU_WEST_1);
        DynamoDBMapper mapper = new DynamoDBMapper(client, new DynamoDBMapperConfig.Builder().withTableNameOverride(TableNameOverride.withTableNameReplacement(System.getenv("DDB_TABLE"))).build());

        Pet dbPet = newPet;
        dbPet.setId(UUID.randomUUID().toString());

        Subsegment subsegment = AWSXRay.beginSubsegment("Save Pet to DynamoDB");
        subsegment.putAnnotation("table", System.getenv("DDB_TABLE"));
        subsegment.putAnnotation("id", dbPet.getId());

        mapper.save(dbPet);

        AWSXRay.endSubsegment();

        return Response.status(200).entity(dbPet).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pet> listPets() {
        client.withRegion(Regions.EU_WEST_1);
        DynamoDBMapper mapper = new DynamoDBMapper(client, new DynamoDBMapperConfig.Builder().withTableNameOverride(TableNameOverride.withTableNameReplacement(System.getenv("DDB_TABLE"))).build());

        List<Pet> outputPets = mapper.scan(Pet.class, new DynamoDBScanExpression());
        return outputPets;
    }

    @Path("/{petId}") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Pet getPetDetails(@PathParam("petId") String petId) {
        client.withRegion(Regions.EU_WEST_1);
        DynamoDBMapper mapper = new DynamoDBMapper(client, new DynamoDBMapperConfig.Builder().withTableNameOverride(TableNameOverride.withTableNameReplacement(System.getenv("DDB_TABLE"))).build());

        Pet newPet = mapper.load(Pet.class, petId);
        return newPet;
    }
}
