package xstring

import "math"

func CutEnd(s string, n int) string {
	m := int(math.Min(float64(n), float64(len(s))))
	return s[len(s)-m:]
}

//func Cut(s string, n int) string {
//	m := int(math.Min(float64(n), float64(len(s))))
//	return s[:m]
//}
