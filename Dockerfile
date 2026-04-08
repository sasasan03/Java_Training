FROM eclipse-temurin:21-jdk

WORKDIR /app

# PostgreSQL JDBCドライバーをダウンロード
ADD https://jdbc.postgresql.org/download/postgresql-42.7.3.jar /app/lib/postgresql.jar

# ソースコードをコピー
COPY src/ /app/src/

# コンパイル（クラスパスにJDBCドライバーを含める）
RUN mkdir -p /app/bin && \
    javac -cp /app/lib/postgresql.jar -d /app/bin /app/src/*.java

CMD ["java", "-cp", "/app/bin:/app/lib/postgresql.jar", "Student"]
