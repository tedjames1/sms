package xstring

import "testing"

func TestCut(t *testing.T) {
	type args struct {
		s string
		n int
	}
	tests := []struct {
		name string
		args args
		want string
	}{
		{args: args{s: "", n: 1}, want: ""},
		{args: args{s: "123", n: 1}, want: "3"},
		{args: args{s: "123", n: 2}, want: "23"},
		{args: args{s: "123", n: 3}, want: "123"},
		{args: args{s: "123", n: 4}, want: "123"},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := CutEnd(tt.args.s, tt.args.n); got != tt.want {
				t.Errorf("Cut() = %v, want %v", got, tt.want)
			}
		})
	}
}
