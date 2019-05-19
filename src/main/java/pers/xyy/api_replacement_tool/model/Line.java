package pers.xyy.api_replacement_tool.model;

import java.util.Objects;

public class Line {

    private String content;
    private Index index;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setIndex(Integer start, Integer end) {
        if (this.index == null)
            this.index = new Index(start, end);
        else {
            this.index.start = start;
            this.index.end = end;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(content, line.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    class Index {
        private Integer start;
        private Integer end;

        public Index(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "Index{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }


}
