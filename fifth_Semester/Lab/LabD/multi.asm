section .data
format db "%d: %s", 10, 0
args_msg db "Number of arguments: %d", 10, 0
debug_msg_string db "Debug: %s", 10, 0
debug_msg_int db "Debug: %d", 10, 0

section .text
global main
extern printf


main:

    push 2
    push debug_msg_int
    call printf
    add esp, 8
