using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    public float MovementSpeed = 3.0f;
    public float Sensitivity = 2.0f;

    private Vector2 MouseAbsolute;

    // Start is called before the first frame update
    void Start()
    {
        Cursor.visible = false;
        Cursor.lockState = CursorLockMode.Confined;

        MouseAbsolute.x = 0.0f;
    }

    // Update is called once per frame
    void Update()
    {
        Vector3 Forward = transform.forward;

        Forward.y = 0.0f;
        Forward.Normalize();

        Forward = Forward * Input.GetAxis("Vertical") * MovementSpeed * Time.deltaTime;

        Vector3 Right = transform.right * Input.GetAxis("Horizontal") * MovementSpeed * Time.deltaTime;

        transform.position += Forward + Right;



        Vector2 mouseDelta = new Vector2(Input.GetAxisRaw("Mouse X"), Input.GetAxisRaw("Mouse Y"));

        mouseDelta *= Sensitivity;

        MouseAbsolute += mouseDelta;
        MouseAbsolute.y = Mathf.Clamp(MouseAbsolute.y, -89.9f, 89.9f);

        transform.localRotation = Quaternion.AngleAxis(-MouseAbsolute.y, Vector3.right);

        Quaternion yRotation = Quaternion.AngleAxis(MouseAbsolute.x, transform.InverseTransformDirection(Vector3.up));
        transform.localRotation *= yRotation;
    }
}
