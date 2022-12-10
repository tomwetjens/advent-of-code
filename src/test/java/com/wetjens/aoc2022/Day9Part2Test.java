package com.wetjens.aoc2022;

import com.wetjens.aoc2022.Day9Part2.Direction;
import com.wetjens.aoc2022.Day9Part2.Rope;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Part2Test {

    @Test
    void U4_1() {
        var rope = Rope.parse("""
                ......
                ......
                ......
                ......
                4321H.""");

        rope.move(Direction.U);

        assertThat(rope).isEqualTo(Rope.parse("""
                ......
                ......
                ......
                ....H.
                4321.."""));
    }

    @Test
    void U4_2() {
        var rope = Rope.parse("""
                ......
                ......
                ......
                ....H.
                4321..""");

        rope.move(Direction.U);

        assertThat(rope).isEqualTo(Rope.parse("""
                ......
                ......
                ....H.
                .4321.
                5....."""));
    }

    @Test
    void U4_3() {
        var rope = Rope.parse("""
                ......
                ......
                ....H.
                .4321.
                5.....""");

        rope.move(Direction.U);

        assertThat(rope).isEqualTo(Rope.parse("""
                ......
                ....H.
                ....1.
                .432..
                5....."""));
    }


    @Test
    void U4_4() {
        var rope = Rope.parse("""
                ......
                ....H.
                ....1.
                .432..
                5.....""");

        rope.move(Direction.U);

        assertThat(rope).isEqualTo(Rope.parse("""
                ....H.
                ....1.
                ..432.
                .5....
                6....."""));
    }

}