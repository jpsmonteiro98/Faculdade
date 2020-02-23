.data

.text
li $t0, 6
sw $t0, x
li $v0, 1
move $a0, $t0
syscall
