FROM gradle:latest
LABEL maintainer "Angel Manzano Alcaraz <angel.manzano.contractor@bbva.com>"

WORKDIR /hancock-sdk-client-java

EXPOSE 3000
EXPOSE 9229
ENTRYPOINT [ "./environment/entry.sh" ]
CMD ["build"]
