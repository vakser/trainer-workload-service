1. Run the following command in the project root (where the Dockerfile is located):

        docker build -t trainer-workload-service .

   -t trainer-workload-service → Tags the image with the name trainer-workload-service.
   . → Refers to the current directory where Dockerfile is present.

   If your JAR file is missing, ensure you build it first:

        mvn clean package -DskipTests

2. Verify the Docker Image
   After building, confirm that the image is created:

       docker images

   You should see an entry like:

                REPOSITORY       TAG       IMAGE ID       CREATED        SIZE
   trainer-workload-service      latest    abc123xyz      10 seconds ago 200MB

3. Run the Container
   Start a container from your image:

       docker run -p 8100:8100 --name trainer-workload-service trainer-workload-service

   -p 8100:8100 → Maps host port 8100 to container port 8100.
   --name trainer-workload-service → Names the container trainer-workload-service.
   trainer-workload-service → Uses the image we just built.

4. Verify the Running Container
   Check if the container is running:

       docker ps

   To start a shell in a running container, use:

       docker exec -it trainer-workload-service sh 

   To check logs:

       docker logs trainer-workload-service

   To stop the container:

       docker stop trainer-workload-service

5. Before running docker-compose, remove container

       docker rm trainer-workload-service  