global main
extern printf
extern fgets
extern stdin
extern time

section .data

STATE dw 0xace1
MASK dw 0xb400


format_byte db "%02hhx", 10, 0
format_word db "%04hx", 0
end_of_line_to_print db 10, 0
debug_msg_string db "Debug string: %s", 10, 0
debug_msg_int db "Debug int: %d", 10, 0
debug_msg_hex db "Debug hex: %02x", 10, 0
end_of_line db 0


x_struct: dw 5
x_num: dw 0xaa, 1,2,0x44,0x4f
y_struct: dw 6
y_num: dw 0xaa, 1,2,3,0x44,0x4f



section .bss
addition_result resb 1200
seed resd 1 ;To store the seed value
first_input_num resb 600
second_input_num resb 600
buffer resb 600
first_rand_num resb 600 ;struct + number
second_rand_num resb 600 ; struct + number



section .text

main:
    push ebp
    mov ebp, esp
    pusha
    mov eax, [ebp + 8] ; argc
    mov ebx, [ebp + 12] ; argv
    cmp eax, 1
    je .defualt_case
    
    mov eax, [ebx + 4] ; Load the first argument (argv[1])
    cmp byte [eax], '-' ; Check if the first character is '-'
    jne .defualt_case ; If not, jump to default case
    cmp byte [eax + 1], 'I' ; Check if the second character is 'I'
    je .input_case ; If so, jump to input case
    cmp byte [eax + 1], 'R' ; Check if the second character is 'R'
    je .random_case ; If so, jump to random case
    jmp .defualt_case ; If neither, jump to default case
    
    
    
    
    .random_case:
        push dword 0 ;Push null as a agrument to the time function
        call time
        add esp, 4
        mov [seed], eax
        mov eax, [seed]   ; Load the seed
        and eax, 0xFFFF   ; Limit it to 16 bits
        mov [STATE], ax   ; Set the initial STATE
        call create_full_random_nums
        mov eax, first_rand_num
        mov ebx, second_rand_num
        jmp .execute_program
    
    .defualt_case:
        mov eax, y_struct
        mov ebx, x_struct
        jmp .execute_program    

    .input_case:
        call get_multi
        mov eax, first_input_num
        mov ebx, second_input_num   
        jmp .execute_program

    .execute_program:
        call MaxMin
        ;print the max number
        mov ecx, dword [eax]
        and ecx, 0x0FF
        mov ecx, [eax - 4]
        
        and ecx, 0xFFFF ; Zero-extend the word to dword
        push eax
        push ecx
        call print_multi
        add esp, 4 ;clean the stack
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
        pop ebp
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

    mov ebx, [ebp + 8 + 4] ; the smaller number
    mov edx, [ebx - 4] ; the size of the smaller number
    and edx, 0xFFFF

    .add_smaller_loop:
        cmp ecx, edx
        je .add_bigger_loop_start

        movzx esi, word [eax + ecx*2 - 2]
        movzx edi, word [ebx + ecx*2 - 2]

        add esi, edi
        adc esi, 0 ; Add the carry
        mov word [eax + ecx*2 - 2], si ; Store the result
        inc ecx

        jmp .add_smaller_loop

    .add_bigger_loop_start:
        pop ecx ; Restore the size of the bigger number
        cmp ecx, edx
        jbe .end_add_loop ; If the bigger number size is equal to or smaller than the smaller number, we're done

        xor edi, edi ; Ensure carry is cleared (it could be set from the smaller loop)

    .add_bigger_loop:
        cmp edx, ecx
        je .end_add_loop

        movzx esi, word [eax + edx*2 - 2]

        adc esi, 0 ; Add the carry
        mov word [eax + edx*2 - 2], si ; Store the result
        inc edx

        jmp .add_bigger_loop

    .end_add_loop:
        pop edi
        pop esi
        pop edx
        pop ecx
        pop ebx
        pop ebp

        ret

create_full_random_nums:
    push ebp
    mov ebp, esp
    pusha

    lea ebx, [first_rand_num] ; address of the first random number
    lea ecx, [second_rand_num] ; address of the second random number
    push ebx
    call rand_num
    pop ebx
    movzx eax, word [STATE] ; the first random number size
    and eax, 0x0FF ; Mask the lower 3 byts of eax
    mov dword [ebx], eax ; Move the lower 4 bits of eax to [ebx]
 
    push ebx
    call rand_num
    pop ebx


    movzx eax, word [STATE] ; the second random number size
    and eax, 0x0FF ; Mask the lower 4 bits of eax
    mov dword [ecx], eax ; Move the lower 4 bits of eax to [ecx]
    
    mov esi, dword [ebx] ; the first random number size 
    mov edi, dword [ecx] ; the second random number size
    
    
    .first_loop:
        cmp esi, 0
        je .second_loop
        
        push ebx
        call rand_num
        pop ebx
        movzx eax, word [STATE] ; the first random number size
        and eax, 0x0FF ; Mask the lower 4 bits of eax

        mov word [ebx + esi*2], ax
        
        dec esi 
        jmp .first_loop

    .second_loop:
        cmp edi, 0
        je .end
        
        push ebx
        call rand_num
        pop ebx

        movzx eax, word [STATE] ; the first random number size
        and eax, 0x0FF ; Mask the lower 4 bits of eax
        mov word [ecx + edi*2], ax
        dec edi
        jmp .second_loop
    .end:
       
        popa
        pop ebp
        ret
    
rand_num:
    mov ax, [STATE]     ; Load the current STATE
    mov bx, [MASK]      ; Load the MASK

    ; Perform LFSR operation
    xor ah, al          ; XOR high byte and low byte of AX
    shr ax, 1           ; Shift STATE one bit to the right
    jc .set_msb         ; If the carry flag (CF) is set, set the MSB to 1

    mov [STATE], ax     ; Save the updated STATE
    ret                 ; Return the new STATE

.set_msb:
    or ax, bx           ; Set the most significant bit (MSB) to 1 using MASK
    mov [STATE], ax     ; Save the updated STATE
    ret       ; Return the new STATE




get_multi:
    push ebp
    mov ebp, esp
    pusha
    call get_multi_1
    call get_multi_2
    popa
    pop ebp
    ret

get_multi_1:
    push ebp              ; Save base pointer
    mov ebp, esp          ; Set up stack frame
    pusha                 ; Save all registers

    ; Read input using fgets
    lea eax, [buffer]   ; Address of input buffer
    push dword [stdin]    ; Push stdin
    push dword 600        ; Maximum size of input
    push eax              ; Push buffer address
    call fgets            ; Call fgets
    add esp, 12           ; Clean up stack (3 args)

    ; Initialize pointers
    xor ecx, ecx          ; Word counter
    xor esi, esi          ; Input index
    lea edi, [first_input_num + 2]      ; Start of output array (x_num)
    
    .process_loop:
        movzx eax, byte [buffer + esi] ; Load a byte from the input buffer
        cmp eax, 0 ; Check if we reached the end of the string
        je .end_process_loop
        cmp eax, 10 ; Check if the next byte is a newline
        je .end_process_loop
        push eax
        call hex_to_byte ; Convert the byte to a number
        add esp, 4 ; Clean up stack
        
        movzx ebx, byte [buffer + esi + 1] ; Load the next byte
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
        mov [edi + ecx], ax ; Store the word in the output array
        inc ecx ; Increment the word counter
        add esi, 2 ; Move to the next byte
        jmp .process_loop
    
    .end_process_loop_odd:
        mov [edi + ecx], ax ; Store the word in the output array
        inc ecx ; Increment the word counter    
        

    .end_process_loop:
        lea eax, [first_input_num] ; Address of input_struct
        mov [eax], cx ; Store the number of words in input_struct
    
        popa
        pop ebp
        ret

get_multi_2:
    push ebp              ; Save base pointer
    mov ebp, esp          ; Set up stack frame
    pusha                 ; Save all registers

    ; Read input using fgets
    lea eax, [buffer]   ; Address of input buffer
    push dword [stdin]    ; Push stdin
    push dword 600        ; Maximum size of input
    push eax              ; Push buffer address
    call fgets            ; Call fgets
    add esp, 12           ; Clean up stack (3 args)

    ; Initialize pointers
    xor ecx, ecx          ; Word counter
    xor esi, esi          ; Input index
    lea edi, [second_input_num + 2]      ; Start of output array (x_num)
    
    .process_loop:
        movzx eax, byte [buffer + esi] ; Load a byte from the input buffer
        cmp eax, 0 ; Check if we reached the end of the string
        je .end_process_loop
        cmp eax, 10 ; Check if the next byte is a newline
        je .end_process_loop
        push eax
        call hex_to_byte ; Convert the byte to a number
        add esp, 4 ; Clean up stack
        
        movzx ebx, byte [buffer + esi + 1] ; Load the next byte
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
        mov [edi + ecx], ax ; Store the word in the output array
        inc ecx ; Increment the word counter
        add esi, 2 ; Move to the next byte
        jmp .process_loop
    
    .end_process_loop_odd:
        mov [edi + ecx], ax ; Store the word in the output array
        inc ecx ; Increment the word counter    
        

    .end_process_loop:
        lea eax, [second_input_num] ; Address of input_struct
        mov [eax], cx ; Store the number of words in input_struct
    
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
    mov edx, dword [eax]
    and edx, 0x0FF
  
    mov ecx, [ebx]
    and ecx, 0x0FF
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

