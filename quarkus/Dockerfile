# https://github.com/quarkusio/quarkus/issues/32045

FROM seepine/alpine-glibc
WORKDIR /work/
COPY ./target/*-runner /work/application
RUN chmod 775 /work
EXPOSE 8081
CMD ["./application"]