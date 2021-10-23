package jmhtests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class StringWriterBenchmark {

    private final static int iterations = 100000;

    @Param({ "1" })
    private int collect = 1;

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public int stringWriter(Blackhole blackhole) throws IOException {
        StringWriter sw = new StringWriter();
        generate(sw);
        int sum = 0;
        for (int i = 0; i < collect; i++) {
            var result = sw.toString();
            sum += result.length();
            blackhole.consume(result);
        }
        return sum;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public int byteArray(Blackhole blackhole) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter sw = new OutputStreamWriter(baos, "UTF-8");
        generate(sw);
        int sum = 0;
        for (int i = 0; i < collect; i++) {
            var result = baos.toString("UTF-8");
            sum += result.length();
            blackhole.consume(result);
        }
        return sum;
    }

    private void generate(Writer writer) throws IOException {
        for (int i = 0; i < iterations; i++) {
            writer.write("test-" + i + "\n");
        }
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}