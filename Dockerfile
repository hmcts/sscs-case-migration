ARG APP_INSIGHTS_AGENT_VERSION=2.5.1

# Application image

FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.2

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/sscs-case-migration.jar /opt/app/

CMD [ "sscs-case-migration.jar" ]
EXPOSE 8088
