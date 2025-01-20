global main
extern printf
extern fgets
extern stdin

section .data
format_byte db "%02hhx", 10, 0
format_word db "%04hx", 0
end_of_line_to_print db 10, 0
debug_msg_string db "Debug string: %s", 10, 0
debug_msg_int db "Debug int: %d", 10, 0
debug_msg_hex db "Debug hex: %02x", 10, 0
end_of_line db 0

x_struct dw 0

y_struct: dw 5
y_num: dw 0xaa, 1,2,0x44,0x4f
z_struct: dw 6
z_num: dw 0xaa, 1,2,3,0x44,0x4f


section .bss
x_num resb 600
x_buffer resb 600



section .text

main:
    pusha
    mov eax, y_struct
    mov ebx, z_struct
    call MaxMin
    ;print the max number
    push ecx
    mov ecx, [eax - 4]
    
    and ecx, 0xFFFF ; Zero-extend the word to dword
    push eax
    push ecx
    call print_multi 
    add esp, 8 ;clean the stack
    pop ecx
    
    ;print the min number
    push ecx
    mov ecx, [ebx - 4]
    
    and ecx, 0xFFFF ; Zero-extend the word to dword
    push ebx
    push ecx
    call print_multi 
    add esp, 8 ;clean the stack
    pop ecx

    ;print the addition result
    push ebx
    push eax
    call add_multi
    add esp, 8 ;clean the stack
    
    push eax
    mov ebx, [eax - 4] 
    and ebx, 0xFFFF 
    push ebx
    call print_multi
    add esp, 8 ;clean the stack

    popa
    ret

add_multi:
    push ebp
    mov ebp, esp
    push ebx
    push ecx
    push edx
    push esi
    push edi
    
    mov eax, [ebp + 8] ; the bigger number
    mov ecx, [eax - 4] ; the size of the bigger number
    and ecx, 0xFFFF 
    push ecx
    xor ecx, ecx ; the index of the addition loop

    mov ebx, [ebp + 8 + 4 ] ; the smaller number
    mov edx, [ebx - 4] ; the size of the smaller number
    and edx, 0xFFFF 

    .add_smaller_loop:
        cmp ecx, edx
        je .add_bigger_loop
       
        movzx esi, byte [eax + ecx*2 -2]
       
       
        movzx edi, byte [ebx + ecx*2 - 2] 
        
     

        add esi, edi
        adc esi, 0 ; Add the carry
        mov word [eax + ecx*2 - 2], si ; Store the result
        inc ecx
        
        jmp .add_smaller_loop

    .add_bigger_loop:
        xor edi, edi ; the carry
        pop ecx
        cmp ecx, edx
        je .end_add_loop
        
        movzx edi, word [eax + edx*2 - 2]

        add edi, esi
        adc edi, 0 ; Add the carry
        mov word [eax + edx*2 - 2], di ; Store the result
        inc edx
       
        push ecx
        jmp .add_bigger_loop

    .end_add_loop:
        pop edi
        pop esi
        pop edx
        pop ecx
        pop ebx
        pop ebp
        ret


get_multi:
    push ebp              ; Save base pointer
    mov ebp, esp          ; Set up stack frame
    pusha                 ; Save all registers

    ; Read input using fgets
    lea eax, [x_buffer]   ; Address of input buffer
    push dword [stdin]    ; Push stdin
    push dword 600        ; Maximum size of input
    push eax              ; Push buffer address
    call fgets            ; Call fgets
    add esp, 12           ; Clean up stack (3 args)

    ; Initialize pointers
    xor ecx, ecx          ; Word counter
    xor esi, esi          ; Input index
    lea edi, [x_num]      ; Start of output array (x_num)
    .process_loop:
        movzx eax, byte [x_buffer + esi] ; Load a byte from the input buffer
        cmp eax, 0 ; Check if we reached the end of the string
        je .end_process_loop
        push eax
        call hex_to_byte ; Convert the byte to a number
        add esp, 4 ; Clean up stack
        
        movzx ebx, byte [x_buffer + esi + 1] ; Load the next byte
        cmp ebx, 0 ; Check if we reached the end of the string
        je .end_process_loop_odd
        cmp ebx, 10 ; Check if the next byte is a newline
        je .end_process_loop_odd
        push eax
        push ebx
        call hex_to_byte ; Convert the byte to a number
        mov ebx, eax ; Store the result in ebx
        add esp, 4
        pop eax ; Restore the first byte
        
        shl ebx, 4 ; Shift the second byte to the left
        add eax, ebx ; Combine the two bytes
        mov [edi + ecx ], ax ; Store the word in the output array
        inc ecx ; Increment the word counter
        add esi, 2 ; Move to the next byte
        jmp .process_loop
    
    .end_process_loop_odd:
        mov [edi + ecx], ax ; Store the word in the output array
        inc ecx ; Increment the word counter
        inc esi ; Move to the next byte
       
        
    .end_process_loop:
        lea eax, [x_struct] ; Address of x_struct
        inc ecx ; Increment the word counter
        mov [eax], cx ; Store the number of words in x_struct
        popa
        pop ebp
        ret


print_multi: ;(x_size, x_num_array address)
    push ebp
    mov ebp, esp   
    pusha
    mov eax, [ebp + 8] ; x_struct size
    mov ebx, [ebp + 8 + 4 ] ; address of x_num
    .print_loop:
        cmp eax, 0 ;check if we reached the end of the struct
        je .end_print_loop ;if so, end the loop
        ;print the argument
        mov esi, [ebx + 2*eax - 4]; get the word from the array x_num
        pusha
        push esi
        push format_word
        call printf
        add esp, 8 ;clean the stack
        popa
        dec eax ;decrement the argument index
        jmp .print_loop
    
    .end_print_loop:
    push end_of_line_to_print
    call printf
    add esp, 4 ;clean the stack for end_of_line
    popa
    pop ebp
    ret





MaxMin:
    push ebp
    mov ebp, esp
    push edx
    push ecx
    mov edx, [eax]
    mov ecx, [ebx]
    cmp edx, ecx
    jg .end_MaxMin ; If [eax] > [ebx], return
    pop ecx
    push ecx
    mov ecx, eax ; Swap eax and ebx
    mov eax, ebx
    mov ebx, ecx
    .end_MaxMin:
        pop ecx
        pop edx
        add eax, 4
        add ebx, 4
        pop ebp
        ret

hex_to_byte:
    push ebp
    mov ebp, esp

    mov al, [ebp + 8] ; Load the input character
    cmp al, '0'
    jl .invalid_input
    cmp al, '9'
    jle .is_digit
    cmp al, 'A'
    jl .invalid_input
    cmp al, 'F'
    jle .is_uppercase
    cmp al, 'a'
    jl .invalid_input
    cmp al, 'f'
    jle .is_lowercase
    jmp .invalid_input

.is_digit:
    sub al, '0'
    jmp .done

.is_uppercase:
    sub al, 'A'
    add al, 10
    jmp .done

.is_lowercase:
    sub al, 'a'
    add al, 10
    jmp .done

.invalid_input:
    xor eax, eax ; Return 0 for invalid input

.done:
    pop ebp
    ret


print_debug_string:
    push ebp
    mov ebp, esp
    pusha
    push dword [ebp + 8]
    push debug_msg_string
    call printf
    add esp, 8 ;clean the stack
    popa
    pop ebp
    ret

print_debug_int:
    push ebp
    mov ebp, esp
    pusha
    push dword [ebp + 8]
    push debug_msg_int
    call printf
    add esp, 8 ;clean the stack
    popa
    pop ebp
    ret

print_debug_hex:
    push ebp
    mov ebp, esp
    pusha
    push dword [ebp + 8]
    push debug_msg_hex
    call printf
    add esp, 8 ;clean the stack
    popa
    pop ebp
    ret

