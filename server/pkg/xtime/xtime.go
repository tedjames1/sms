package xtime

import "time"

func CurFormatedTime() string {
	now := time.Now()
	return now.Format("2006-01-02 15:04:05.000000")
}
