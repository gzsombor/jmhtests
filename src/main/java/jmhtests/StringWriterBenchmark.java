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

@State(Scope.Benchmark)
public class StringWriterBenchmark {

    private final static int iterations = 10000;

    @Param({ "100", "1000", "10000" })
    private int collect = 100;

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public int stringWriter() throws IOException {
        StringWriter sw = new StringWriter();
        generate(sw);
        int sum = 0;
        for (int i = 0; i < collect; i++) {
            var result = sw.toString();
            sum += result.length();
        }
        return sum;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public int byteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter sw = new OutputStreamWriter(baos);
        generate(sw);
        int sum = 0;
        for (int i = 0; i < collect; i++) {
            var result = baos.toString("UTF-8");
            sum += result.length();
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