# You should always specify a full version here to ensure all of your developers
# are running the same version of Node.
FROM node:7.8.0

# The base node image sets a very verbose log level.
ENV NPM_CONFIG_LOGLEVEL warn

# Copy all local files into the image.
COPY build build

# Install `serve` to run the application.
RUN yarn global add serve

# Set the command to start the node server.
CMD serve build --port 80

# Tell Docker about the port we'll run on.
EXPOSE 80